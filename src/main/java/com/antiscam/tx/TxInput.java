package com.antiscam.tx;

import com.antiscam.enums.Algorithm;
import com.antiscam.util.ByteUtil;
import com.antiscam.util.EncryptUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * 交易输入
 *
 * @author wuming
 */
public class TxInput {

    /**
     * 交易Id
     */
    private byte[] txId;
    /**
     * 交易输出索引
     */
    private int    txOutputIndex;
    /**
     * 签名
     */
    private byte[] signature;
    /**
     * SHA256(公钥)哈希值
     */
    private byte[] publicKey;

    private TxInput() {
    }

    /**
     * 构造交易输入
     *
     * @param txId          交易Id
     * @param txOutputIndex 交易输出索引
     * @param signature     签名
     * @param publicKey     公钥
     */
    TxInput(byte[] txId, int txOutputIndex, byte[] signature, byte[] publicKey) {
        this.txId = txId;
        this.txOutputIndex = txOutputIndex;
        this.signature = signature;
        this.publicKey = publicKey;
    }

    /**
     * 检查公钥hash是否用于交易输入
     *
     * @param publicKeyHash 公钥hash
     * @return true: 是, false: 不是
     */
    public boolean usesKey(byte[] publicKeyHash) {
        byte[] lockingHash = EncryptUtil.hash(this.publicKey, Algorithm.RIPEMD160);
        return Arrays.equals(lockingHash, publicKeyHash);
    }

    @Override
    public String toString() {
        try {
            return "txId:" + ByteUtil.toString(txId) + ",txOutputIndex:" + txOutputIndex + ",signature:"
                    + ByteUtil.toString(signature) + ",publicKey:" + ByteUtil.toString(publicKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return super.toString();
        }
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
     * Getter for property 'txOutputIndex'.
     *
     * @return Value for property 'txOutputIndex'.
     */
    public int getTxOutputIndex() {
        return txOutputIndex;
    }

    /**
     * Getter for property 'publicKey'.
     *
     * @return Value for property 'publicKey'.
     */
    public byte[] getPublicKey() {
        return ArrayUtils.clone(publicKey);
    }

    /**
     * Getter for property 'signature'.
     *
     * @return Value for property 'signature'.
     */
    public byte[] getSignature() {
        return ArrayUtils.clone(signature);
    }

    /**
     * Setter for property 'signature'.
     *
     * @param signature Value to set for property 'signature'.
     */
    public void setSignature(byte[] signature) {
        this.signature = ArrayUtils.clone(signature);
    }

    /**
     * Setter for property 'publicKey'.
     *
     * @param publicKey Value to set for property 'publicKey'.
     */
    public void setPublicKey(byte[] publicKey) {
        this.publicKey = ArrayUtils.clone(publicKey);
    }
}
