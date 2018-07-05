package com.antiscam.tx;

import com.antiscam.enums.Algorithm;
import com.antiscam.util.Base58Util;
import com.antiscam.util.EncryptUtil;
import org.apache.commons.lang3.ArrayUtils;

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
     * 未压缩公钥
     */
    private byte[] uncompressedPublicKey;

    private TxInput() {
    }

    /**
     * 构造交易输入
     *
     * @param txId                  交易Id
     * @param txOutputIndex         交易输出索引
     * @param signature             签名
     * @param uncompressedPublicKey 未压缩公钥
     */
    TxInput(byte[] txId, int txOutputIndex, byte[] signature, byte[] uncompressedPublicKey) {
        this.txId = txId;
        this.txOutputIndex = txOutputIndex;
        this.signature = signature;
        this.uncompressedPublicKey = uncompressedPublicKey;
    }

    /**
     * 检查公钥hash是否用于交易输入
     *
     * @param publicKeyHash 公钥hash
     * @return true: 是, false: 不是
     */
    public boolean usesKey(byte[] publicKeyHash) {
        byte[] lockingHash = EncryptUtil.hash(EncryptUtil.hash(this.uncompressedPublicKey, Algorithm.SHA256), Algorithm.RIPEMD160);
        return Arrays.equals(lockingHash, publicKeyHash);
    }

    @Override
    public String toString() {
        return "\"txId\": \"" + Base58Util.encode(txId) + "\",\"txOutputIndex\":" + txOutputIndex + ",\"uncompressedPublicKey\": \"" + Base58Util.encode(uncompressedPublicKey) + "\"";
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
     * Getter for property 'uncompressedPublicKey'.
     *
     * @return Value for property 'uncompressedPublicKey'.
     */
    public byte[] getUncompressedPublicKey() {
        return uncompressedPublicKey;
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
     * Setter for property 'uncompressedPublicKey'.
     *
     * @param uncompressedPublicKey Value to set for property 'uncompressedPublicKey'.
     */
    public void setUncompressedPublicKey(byte[] uncompressedPublicKey) {
        this.uncompressedPublicKey = ArrayUtils.clone(uncompressedPublicKey);
    }
}
