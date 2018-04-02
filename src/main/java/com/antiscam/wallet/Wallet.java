package com.antiscam.wallet;

import com.antiscam.constant.Constant;
import com.antiscam.enums.Algorithm;
import com.antiscam.util.Base58Util;
import com.antiscam.util.EncryptUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.security.*;

/**
 * 钱包
 *
 * @author wuming
 */
public class Wallet {
    /**
     * 私钥
     */
    private BCECPrivateKey privateKey;
    /**
     * SHA256(公钥)哈希值
     */
    private byte[]         publicKey;

    Wallet() {
        KeyPair keyPair = newECKeyPair();
        // 私钥
        this.privateKey = (BCECPrivateKey) keyPair.getPrivate();

        // 公钥hash
        byte[] publicKeyBytes = ((BCECPublicKey) keyPair.getPublic()).getQ().getEncoded(false);
        this.publicKey = EncryptUtil.hash(publicKeyBytes, Algorithm.SHA256);
    }

    /**
     * 创建密钥对
     *
     * @return 密钥对
     */
    private KeyPair newECKeyPair() {
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

    /**
     * 获取钱包地址
     *
     * @return 钱包地址
     */
    public String getAddress() {
        byte[] publicKeyHash = EncryptUtil.hash(this.publicKey, Algorithm.RIPEMD160);
        return Base58Util.encodeChecked(Constant.version, publicKeyHash);
    }

    /**
     * Getter for property 'privateKey'.
     *
     * @return Value for property 'privateKey'.
     */
    public BCECPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * Getter for property 'publicKey'.
     *
     * @return Value for property 'publicKey'.
     */
    public byte[] getPublicKey() {
        return ArrayUtils.clone(publicKey);
    }
}
