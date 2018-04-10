package com.antiscam.util;

import com.antiscam.enums.Algorithm;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.security.*;

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

    /**
     * 获取签名算法
     *
     * @param algorithm 算法
     * @return 签名算法
     * @throws NoSuchProviderException  异常
     * @throws NoSuchAlgorithmException 异常
     */
    public static Signature getSignature(Algorithm algorithm) throws NoSuchProviderException, NoSuchAlgorithmException {
        return Signature.getInstance(algorithm.getName(), BouncyCastleProvider.PROVIDER_NAME);
    }

    /**
     * 获取密钥工厂
     *
     * @param algorithm 算法
     * @return 密钥工厂
     * @throws NoSuchProviderException  异常
     * @throws NoSuchAlgorithmException 异常
     */
    public static KeyFactory getKeyFactory(Algorithm algorithm) throws NoSuchProviderException, NoSuchAlgorithmException {
        return KeyFactory.getInstance(algorithm.getName(), BouncyCastleProvider.PROVIDER_NAME);
    }

    /**
     * 创建密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() {
        try {
            // 注册 BC Provider
            Security.addProvider(new BouncyCastleProvider());
            // 创建椭圆曲线算法的密钥对生成器, 算法为 ECDSA
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Algorithm.ECDSA.getName(), BouncyCastleProvider.PROVIDER_NAME);
            // 椭圆曲线（EC）域参数设定
            // bitcoin 为什么会选择 secp256k1, 详见：https://bitcointalk.org/index.php?topic=151120.0
            ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
            keyPairGenerator.initialize(ecSpec, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
