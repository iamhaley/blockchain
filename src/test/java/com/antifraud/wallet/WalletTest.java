package com.antifraud.wallet;

import com.antiscam.enums.Algorithm;
import com.antiscam.util.EncryptUtil;
import com.antiscam.wallet.Wallet;
import com.antiscam.wallet.WalletHandler;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.Test;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class WalletTest {

    /**
     * 签名验证单元测试
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        Wallet wallet = WalletHandler.getWallet();

        byte[] bytes = new byte[]{1};

        PrivateKey privateKey = wallet.getPrivateKey();
        Signature  signature  = EncryptUtil.getSignature(Algorithm.SHA256WITHECDSA);
        signature.initSign(privateKey);
        signature.update(bytes);
        byte[] sign = signature.sign();

        byte[]          uncompressedPublicKey = wallet.getUncompressedPublicKey();
        ECParameterSpec ecSpec                = ECNamedCurveTable.getParameterSpec("secp256k1");
        KeyFactory      keyFactory            = EncryptUtil.getKeyFactory(Algorithm.ECDSA);
        Signature       verify                = EncryptUtil.getSignature(Algorithm.SHA256WITHECDSA);
        byte[]          x                     = new byte[32];
        byte[]          y                     = new byte[32];
        System.out.println(uncompressedPublicKey.length);
        System.arraycopy(uncompressedPublicKey, 1, x, 0, 32);
        System.arraycopy(uncompressedPublicKey, 33, y, 0, 32);
        // 为椭圆曲线添加(x,y)点参数
        ECPoint         ecPoint   = ecSpec.getCurve().createPoint(new BigInteger(1, x), new BigInteger(1, y));
        ECPublicKeySpec keySpec   = new ECPublicKeySpec(ecPoint, ecSpec);
        PublicKey       publicKey = keyFactory.generatePublic(keySpec);
        verify.initVerify(publicKey);
        verify.update(bytes);

        System.out.println(verify.verify(sign));
    }

}
