package com.antiscam.util;

import com.antiscam.enums.Algorithm;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * 加密工具类
 *
 * @author wuming
 */
public class EncryptUtil {

    static {
        // 加入BouncyCastleProvider的支持
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 哈希
     *
     * @param data      数据字节数组
     * @param algorithm 哈希算法 @see com.antiscam.enums.Algorithm
     * @return 哈希值
     */
    public static byte[] hash(byte[] data, Algorithm algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.getName());
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    /**
     * 双哈希
     *
     * @return 两次哈希后的哈希值
     */
    public static byte[] hashTwice(byte[] data, Algorithm algorithm, int offset, int length) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.getName());
            md.update(data, offset, length);
            return md.digest(md.digest(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 双哈希
     *
     * @return 两次哈希后的哈希值
     */
    public static byte[] hashTwice(byte[] data, Algorithm algorithm) {
        return hashTwice(data, algorithm, 0, data.length);
    }

}
