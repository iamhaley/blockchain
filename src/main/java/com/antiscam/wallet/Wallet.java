package com.antiscam.wallet;

import com.antiscam.constant.Constant;
import com.antiscam.enums.Algorithm;
import com.antiscam.util.Base58Util;
import com.antiscam.util.EncryptUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.security.PrivateKey;

/**
 * 钱包
 *
 * @author wuming
 */
public class Wallet implements Serializable {
    private static final long serialVersionUID = -709175281626338937L;
    /**
     * 私钥
     */
    private PrivateKey privateKey;
    /**
     * 未压缩公钥
     */
    private byte[]     uncompressedPublicKey;

    Wallet() {
    }

    /**
     * 构建钱包
     *
     * @param privateKey            私钥
     * @param uncompressedPublicKey 未压缩公钥
     */
    Wallet(PrivateKey privateKey, byte[] uncompressedPublicKey) {
        this.privateKey = privateKey;
        this.uncompressedPublicKey = uncompressedPublicKey;
    }

    /**
     * 获取钱包地址
     *
     * @return 钱包地址
     */
    public String getAddress() {
        byte[] publicKeyHash = EncryptUtil.hash(EncryptUtil.hash(this.uncompressedPublicKey, Algorithm.SHA256), Algorithm.RIPEMD160);
        return Base58Util.encodeChecked(Constant.version, publicKeyHash);
    }

    /**
     * 获取公钥hash
     *
     * @return 公钥hash
     */
    public byte[] getPublicKeyHash() {
        return EncryptUtil.hash(this.uncompressedPublicKey, Algorithm.SHA256);
    }

    /**
     * Getter for property 'privateKey'.
     *
     * @return Value for property 'privateKey'.
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * Getter for property 'uncompressedPublicKey'.
     *
     * @return Value for property 'uncompressedPublicKey'.
     */
    public byte[] getUncompressedPublicKey() {
        return ArrayUtils.clone(uncompressedPublicKey);
    }
}
