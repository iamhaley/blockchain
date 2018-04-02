package com.antiscam.tx;

import com.antiscam.util.ByteUtil;
import com.antiscam.wallet.WalletHandler;
import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
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
     * RipeMD160(SHA256(公钥))哈希值
     */
    private byte[] publicKeyHash;

    private TxOutput() {
    }

    /**
     * 构造交易输出
     *
     * @param value         价值
     * @param publicKeyHash 公钥hash
     */
    TxOutput(int value, byte[] publicKeyHash) {
        this.value = value;
        this.publicKeyHash = publicKeyHash;
    }

    /**
     * 构造交易输出
     *
     * @param value   价值
     * @param address 钱包地址
     */
    TxOutput(int value, String address) {
        this.value = value;
        this.publicKeyHash = WalletHandler.getPublicKeyHash(address);
    }

    /**
     * 检查指定公钥与交易输出是否匹配
     *
     * @param publicKeyHash 公钥hash
     * @return true: 匹配, false: 不匹配
     */
    public boolean isLockedWith(byte[] publicKeyHash) {
        return Arrays.equals(this.publicKeyHash, publicKeyHash);
    }

    @Override
    public String toString() {
        try {
            return "value:" + value + ",publicKeyHash" + ByteUtil.toString(publicKeyHash);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return super.toString();
        }
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
     * Getter for property 'publicKeyHash'.
     *
     * @return Value for property 'publicKeyHash'.
     */
    public byte[] getPublicKeyHash() {
        return ArrayUtils.clone(publicKeyHash);
    }
}
