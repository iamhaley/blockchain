package com.antiscam.tx;

import com.antiscam.enums.Algorithm;
import com.antiscam.util.Base58Util;
import com.antiscam.util.EncryptUtil;
import com.antiscam.wallet.WalletHandler;

import java.util.Arrays;

/**
 * 交易输出
 *
 * @author wuming
 */
public class TxOutput {

    /**
     * 价值
     */
    private int    value;
    /**
     * 未压缩公钥
     */
    private byte[] uncompressedPublicKey;

    private TxOutput() {
    }

    /**
     * 构造交易输出
     *
     * @param value                 价值
     * @param uncompressedPublicKey 未压缩公钥
     */
    TxOutput(int value, byte[] uncompressedPublicKey) {
        this.value = value;
        this.uncompressedPublicKey = uncompressedPublicKey;
    }

    /**
     * 构造交易输出
     *
     * @param value   价值
     * @param address 钱包地址
     */
    TxOutput(int value, String address) {
        this.value = value;
        this.uncompressedPublicKey = WalletHandler.getUncompressedPublicKey(address);
    }

    /**
     * 检查指定公钥与交易输出是否匹配
     *
     * @param publicKeyHash 公钥hash
     * @return true: 匹配, false: 不匹配
     */
    public boolean isLockedWith(byte[] publicKeyHash) {
        return Arrays.equals(EncryptUtil.hash(EncryptUtil.hash(this.uncompressedPublicKey, Algorithm.SHA256), Algorithm.RIPEMD160), publicKeyHash);
    }

    @Override
    public String toString() {
        return "\"value\":" + value + ",\"uncompressedPublicKey\": \"" + Base58Util.encode(uncompressedPublicKey) + "\"";
    }

    /**
     * Getter for property 'value'.
     *
     * @return Value for property 'value'.
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter for property 'uncompressedPublicKey'.
     *
     * @return Value for property 'uncompressedPublicKey'.
     */
    public byte[] getUncompressedPublicKey() {
        return uncompressedPublicKey;
    }
}
