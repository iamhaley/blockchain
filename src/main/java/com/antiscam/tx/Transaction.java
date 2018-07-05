package com.antiscam.tx;

import com.antiscam.block.Blockchain;
import com.antiscam.enums.Algorithm;
import com.antiscam.util.*;
import com.antiscam.wallet.Wallet;
import com.antiscam.wallet.WalletHandler;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.UUID;

/**
 * 交易
 *
 * @author wuming
 */
public class Transaction {
    /**
     * 交易ID
     */
    byte[]     txId;
    /**
     * 交易输入
     */
    TxInput[]  inputs;
    /**
     * 交易输出
     */
    TxOutput[] outputs;
    /**
     * 是否首笔
     */
    boolean coinbase = false;

    Transaction() {
    }

    /**
     * 构造交易
     *
     * @param inputs  交易输入
     * @param outputs 交易输出
     */
    private Transaction(TxInput[] inputs, TxOutput[] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    /**
     * 构造交易
     *
     * @param txId    交易ID
     * @param inputs  交易输入
     * @param outputs 交易输出
     */
    private Transaction(byte[] txId, TxInput[] inputs, TxOutput[] outputs) {
        this.txId = txId;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    /**
     * 创建未花费输出交易
     *
     * @param fromAddress 源地址
     * @param toAddress   目标气质
     * @param amount      价值
     * @param bc          链
     * @return 交易
     */
    public static Transaction buildUTXOTransaction(String fromAddress, String toAddress, int amount, Blockchain bc) throws DecoderException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
        Bill bill = bc.buildBill(fromAddress, amount);
        AssertUtil.check(bill.getAccumulated() >= amount, "transaction failed, not enough funds");

        Wallet fromWallet = WalletHandler.getWallet(fromAddress);

        // 交易输入
        TxInput[] inputs = {};
        for (Map.Entry<String, int[]> entry : bill.getOutputsMap().entrySet()) {
            String txIdHexStr      = entry.getKey();
            int[]  txOutputIndexes = entry.getValue();
            byte[] txId            = Hex.decodeHex(txIdHexStr);
            for (int txOutputIndex : txOutputIndexes) {
                inputs = ArrayUtils.add(inputs, new TxInput(txId, txOutputIndex, null, fromWallet.getUncompressedPublicKey()));
            }
        }

        // 交易输出
        TxOutput[] outputs = {};
        outputs = ArrayUtils.add(outputs, new TxOutput(amount, toAddress));
        // 找零
        if (bill.getAccumulated() > amount) {
            outputs = ArrayUtils.add(outputs, new TxOutput((bill.getAccumulated() - amount), fromAddress));
        }

        Transaction transaction = new Transaction(inputs, outputs);
        transaction.setTxId(transaction.hash());

        // 交易签名
        bc.signTransaction(fromWallet.getPrivateKey(), transaction);

        return transaction;
    }

    /**
     * 获取交易hash
     * <p>
     * 不包含交易ID
     *
     * @return 交易hash
     */
    public byte[] hash() {
        byte[]      serializeBytes = SerializeUtil.serialize(this);
        Transaction deepCopyTx     = (Transaction) SerializeUtil.deserialize(serializeBytes);
        deepCopyTx.setTxId(new byte[]{});
        return EncryptUtil.hash(SerializeUtil.serialize(deepCopyTx), Algorithm.SHA256);
    }

    /**
     * 签名
     *
     * @param privateKey    私钥
     * @param previousTxMap 前面多笔交易集合
     */
    public void sign(PrivateKey privateKey, Map<String, Transaction> previousTxMap) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // coinbase 不需要签名, 因为它不存在交易输入
        if (isCoinbase()) {
            return;
        }
        // 获取当前交易副本
        Transaction trimmedTxCopy = trimmedTxCopy();

        Signature signature = EncryptUtil.getSignature(Algorithm.SHA256WITHECDSA);
        signature.initSign(privateKey);

        for (int txCopyInputIndex = 0; txCopyInputIndex < trimmedTxCopy.inputs.length; txCopyInputIndex++) {
            TxInput txCopyInput = trimmedTxCopy.inputs[txCopyInputIndex];

            // 获取指向交易输入的交易输出
            String      txIdHexStr = Hex.encodeHexString(txCopyInput.getTxId());
            Transaction previousTx = previousTxMap.get(txIdHexStr);
            // 准备签名数据
            preparedSignature(trimmedTxCopy, txCopyInput, previousTx);

            // 交易副本签名, 即对交易副本ID签名
            // 交易副本ID: 交易输入包含公钥而不包含签名的交易副本的hash
            signature.update(trimmedTxCopy.getTxId());
            byte[] sign = signature.sign();

            // 为当前交易输入设置交易签名
            this.inputs[txCopyInputIndex].setSignature(sign);
        }
    }

    /**
     * 验签
     *
     * @param previousTxMap 前面多笔交易集合
     * @return true: 验签通过, false: 未通过
     * @throws NoSuchProviderException  异常
     * @throws NoSuchAlgorithmException 异常
     * @throws InvalidKeySpecException  异常
     * @throws InvalidKeyException      异常
     * @throws SignatureException       异常
     */
    public boolean verify(Map<String, Transaction> previousTxMap) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        // 获取当前交易副本
        Transaction trimmedTxCopy = trimmedTxCopy();

        ECParameterSpec ecSpec     = ECNamedCurveTable.getParameterSpec("secp256k1");
        KeyFactory      keyFactory = EncryptUtil.getKeyFactory(Algorithm.ECDSA);
        Signature       verify     = EncryptUtil.getSignature(Algorithm.SHA256WITHECDSA);

        boolean verified = true;

        for (int txCopyInputIndex = 0; txCopyInputIndex < trimmedTxCopy.inputs.length; txCopyInputIndex++) {
            TxInput txCopyInput = trimmedTxCopy.inputs[txCopyInputIndex];
            // 获取指向交易输入的交易输出
            String      txIdHexStr = Hex.encodeHexString(txCopyInput.getTxId());
            Transaction previousTx = previousTxMap.get(txIdHexStr);
            // 准备签名数据
            preparedSignature(trimmedTxCopy, txCopyInput, previousTx);
            // 获取公钥
            PublicKey publicKey = getPublicKey(this.inputs[txCopyInputIndex].getUncompressedPublicKey(), ecSpec, keyFactory);
            // 验签
            verify.initVerify(publicKey);
            verify.update(trimmedTxCopy.getTxId());
            if (!verify.verify(this.inputs[txCopyInputIndex].getSignature())) {
                verified = false;
            }
        }

        return verified;
    }

    /**
     * 根据公钥hash获取公钥
     *
     * @param uncompressedPublicKey 未压缩公钥
     * @param ecSpec                椭圆曲线参数
     * @param keyFactory            KeyFactory
     * @return 公钥
     */
    private PublicKey getPublicKey(byte[] uncompressedPublicKey, ECParameterSpec ecSpec, KeyFactory keyFactory) throws InvalidKeySpecException {
        // publicKeyHash: [0x04, x coord of point (32 bytes), y coord of point (32 bytes)]
        // 第一个字节为 0x04
        byte[] x = new byte[32];
        byte[] y = new byte[32];
        System.arraycopy(uncompressedPublicKey, 1, x, 0, 32);
        System.arraycopy(uncompressedPublicKey, 33, y, 0, 32);
        // 为椭圆曲线添加(x,y)点参数
        ECPoint         ecPoint = ecSpec.getCurve().createPoint(new BigInteger(1, x), new BigInteger(1, y));
        ECPublicKeySpec keySpec = new ECPublicKeySpec(ecPoint, ecSpec);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 准备签名数据
     *
     * @param trimmedTxCopy 交易副本
     * @param txCopyInput   交易副本输入
     * @param previousTx    交易副本输入在前面多笔交易中的交易
     */
    private void preparedSignature(Transaction trimmedTxCopy, TxInput txCopyInput, Transaction previousTx) {
        // 获取指向交易输入的交易输出
        int      txCopyOutputIndex = txCopyInput.getTxOutputIndex();
        TxOutput previousTxOutput  = previousTx.outputs[txCopyOutputIndex];
        // 设置交易副本的输入公钥, 为了二次确认输入副本中还不应有签名, 设置签名为null
        txCopyInput.setUncompressedPublicKey(previousTxOutput.getUncompressedPublicKey());
        txCopyInput.setSignature(null);
        // 设置交易副本ID, 即包含公钥而不含签名的交易hash
        trimmedTxCopy.setTxId(trimmedTxCopy.hash());
        // 重置交易副本输入为null, 防止对后面的交易输入产生影响
        txCopyInput.setUncompressedPublicKey(null);
    }

    /**
     * 交易副本
     * <p>
     * 交易输入不包含签名与公钥
     *
     * @return 交易副本
     */
    public Transaction trimmedTxCopy() {
        // 交易输入, 签名与公钥设为null
        TxInput[] inputs = {};
        for (TxInput input : this.inputs) {
            inputs = ArrayUtils.add(inputs, new TxInput(input.getTxId(), input.getTxOutputIndex(), null, null));
        }
        // 交易输出
        TxOutput[] outputs = {};
        for (TxOutput output : this.outputs) {
            outputs = ArrayUtils.add(outputs, new TxOutput(output.getValue(), output.getUncompressedPublicKey()));
        }
        // 返回交易副本
        return new Transaction(this.txId, inputs, outputs);
    }

    /**
     * 生成交易Id
     *
     * @return 交易Id
     */
    byte[] generateTxId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        try {
            return ByteUtil.toBytes(uuid);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "\"txId\": \"" + Base58Util.encode(txId) + "\",\"isCoinBase\":" + isCoinbase() + ",\"inputs\":" + ArrayUtils.toString(inputs) + ",\"outputs\":" + ArrayUtils.toString(outputs);
    }

    /**
     * Getter for property 'txId'.
     *
     * @return Value for property 'txId'.
     */
    public byte[] getTxId() {
        return ArrayUtils.clone(txId);
    }

    /**
     * Getter for property 'inputs'.
     *
     * @return Value for property 'inputs'.
     */
    public TxInput[] getInputs() {
        return ArrayUtils.clone(inputs);
    }

    /**
     * Getter for property 'outputs'.
     *
     * @return Value for property 'outputs'.
     */
    public TxOutput[] getOutputs() {
        return ArrayUtils.clone(outputs);
    }

    /**
     * Getter for property 'coinbase'.
     *
     * @return Value for property 'coinbase'.
     */
    public boolean isCoinbase() {
        return coinbase;
    }

    /**
     * Setter for property 'txId'.
     *
     * @param txId Value to set for property 'txId'.
     */
    public void setTxId(byte[] txId) {
        this.txId = ArrayUtils.clone(txId);
    }
}
