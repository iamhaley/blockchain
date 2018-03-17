package com.antiscam.tx;

import com.antiscam.core.Blockchain;
import com.antiscam.util.ByteUtil;
import com.sun.tools.javac.util.Assert;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
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
        this.txId = generateTxId();
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
    public static Transaction buildUTXOTransaction(String fromAddress, String toAddress, int amount, Blockchain bc) throws DecoderException {
        Bill bill = bc.buildBill(fromAddress, amount);
        Assert.check(bill.getAccumulated() >= amount, "transaction failure, not enough funds");

        // 交易输入
        TxInput[] inputs = {};
        for (Map.Entry<String, int[]> entry : bill.getOutputsMap().entrySet()) {
            String txIdHexStr      = entry.getKey();
            int[]  txOutputIndexes = entry.getValue();

            byte[] txId = Hex.decodeHex(txIdHexStr);
            for (int txOutputIndex : txOutputIndexes) {
                inputs = ArrayUtils.add(inputs, new TxInput(txId, txOutputIndex, fromAddress));
            }
        }

        // 交易输出
        TxOutput[] outputs = {};
        outputs = ArrayUtils.add(outputs, new TxOutput(amount, toAddress));
        if (bill.getAccumulated() > amount) {
            outputs = ArrayUtils.add(outputs, new TxOutput((bill.getAccumulated() - amount), fromAddress));
        }

        return new Transaction(inputs, outputs);
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
        try {
            return "txId:" + ByteUtil.toString(txId) + ",inputs:" + ArrayUtils.toString(inputs) + ",outputs:" + ArrayUtils.toString(outputs);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return super.toString();
        }
    }
}
