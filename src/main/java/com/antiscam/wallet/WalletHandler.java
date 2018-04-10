package com.antiscam.wallet;

import com.antiscam.util.*;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.*;

/**
 * 钱包处理器
 */
public class WalletHandler {
    /**
     * 钱包文件
     */
    private static final String              WALLET_FILE = "wallet.dat";
    /**
     * 地址-->钱包
     */
    private static final Map<String, Wallet> walletMap   = new HashMap<>();

    static {
        initLoad();
    }

    private WalletHandler() {
    }

    /**
     * 获取钱包
     *
     * @return 钱包
     */
    public static Wallet getWallet() {
        KeyPair    keyPair               = EncryptUtil.getKeyPair();
        PrivateKey privateKey            = keyPair.getPrivate();
        byte[]     uncompressedPublicKey = ((BCECPublicKey) keyPair.getPublic()).getQ().getEncoded(false);

        Wallet wallet  = new Wallet(privateKey, uncompressedPublicKey);
        String address = wallet.getAddress();
        walletMap.put(address, wallet);

        flush();
        return wallet;
    }

    /**
     * 根据地址获取钱包
     *
     * @param address 地址
     * @return 钱包
     */
    public static Wallet getWallet(String address) {
        Base58Util.decodeChecked(address);
        return walletMap.get(address);
    }

    /**
     * 获取所有钱包地址
     *
     * @return 所有钱包地址
     */
    public static List<String> getAllAddress() {
        List<String> addresses = new ArrayList<>(walletMap.keySet().size());
        addresses.addAll(walletMap.keySet());
        return addresses;
    }

    /**
     * 获取地址公钥hash
     *
     * @param address 地址
     * @return 公钥hash
     */
    public static byte[] getPublicKeyHash(String address) {
        // 1 byte version version + publicKeyHash
        byte[] decodeAddress = Base58Util.decodeChecked(address);
        // publicKeyHash copy from decode address without first 1 byte version
        return Arrays.copyOfRange(decodeAddress, 1, decodeAddress.length);
    }

    /**
     * 获取未压缩公钥
     *
     * @param address 地址
     * @return 未压缩公钥
     */
    public static byte[] getUncompressedPublicKey(String address) {
        // check address
        Base58Util.decodeChecked(address);

        Wallet wallet = getWallet(address);
        AssertUtil.check(null != wallet);
        return wallet.getUncompressedPublicKey();
    }

    /**
     * 将钱包存储至文件
     */
    private static void flush() {
        if (walletMap.size() == 0) {
            return;
        }
        FileUtil.write(WALLET_FILE, SerializeUtil.serialize(walletMap));
    }

    /**
     * 从文件读取钱包
     */
    @SuppressWarnings("unchecked")
    private static void initLoad() {
        byte[] fileBytes = FileUtil.read(WALLET_FILE);
        if (null == fileBytes || fileBytes.length == 0) {
            return;
        }
        Map<String, Wallet> loadWalletMap = (HashMap<String, Wallet>) SerializeUtil.deserialize(fileBytes);
        // 确保 wallet 第一次从文件读取
        walletMap.clear();
        walletMap.putAll(loadWalletMap);
    }
}
