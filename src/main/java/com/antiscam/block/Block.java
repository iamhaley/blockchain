package com.antiscam.block;

import com.antiscam.enums.Algorithm;
import com.antiscam.tx.Transaction;
import com.antiscam.util.AssertUtil;
import com.antiscam.util.EncryptUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

/**
 * 块结构
 *
 * @author wuming
 */
public class Block {
    /**
     * 头信息
     */
    BlockHeader   header;
    /**
     * 交易
     */
    Transaction[] transactions;

    /** Constructs a new Block. */
    Block() {
    }

    /**
     * 构造块
     *
     * @param header       头信息
     * @param transactions 交易
     */
    private Block(BlockHeader header, Transaction[] transactions) {
        this.header = header;
        this.transactions = transactions;
    }

    /**
     * * 获取普通块
     *
     * @param previousHash 前一区块hash
     * @param transactions 交易
     * @return 普通块
     * @throws IOException 异常
     */
    static Block getInstance(byte[] previousHash, Transaction[] transactions) throws IOException {
        AssertUtil.check(null != previousHash && previousHash.length > 0);
        AssertUtil.check(null != transactions && transactions.length > 0);

        long timestamp = System.currentTimeMillis();
        return new Block(new BlockHeader(previousHash, timestamp), transactions);
    }

    /**
     * 获取当前hash值
     *
     * @return 当前hash值
     */
    public byte[] getHash() {
        return this.header.getHash();
    }

    /**
     * 获取前一区块hash值
     *
     * @return 前一区块hash值
     */
    public byte[] getPreviousHash() {
        return this.header.getPreviousHash();
    }

    /**
     * 获取当前区块时间戳
     *
     * @return 当前区块时间戳
     */
    public long getTimestamp() {
        return this.header.getTimestamp();
    }

    /**
     * Getter for property 'transactions'.
     *
     * @return Value for property 'transactions'.
     */
    public Transaction[] getTransactions() {
        return ArrayUtils.clone(transactions);
    }

    /**
     * 获取交易hash, 即交易默克尔根
     * 每次获取会重新计算默克尔根
     *
     * @return 交易hash
     * @throws IOException 异常
     */
    byte[] getTransactionsHash() throws IOException {
        AssertUtil.check(null != this.transactions && this.transactions.length > 0);

        byte[][] txHashes = new byte[this.transactions.length][];
        for (Transaction transaction : this.transactions) {
            byte[] txHash = EncryptUtil.hash(transaction.getTxId(), Algorithm.SHA256);
            txHashes = ArrayUtils.add(txHashes, txHash);
        }
        MerkleTree merkleTree = new MerkleTree(txHashes);
        merkleTree.buildTree();
        this.header.setMerkleTreeRoot(merkleTree.getRoot());
        return merkleTree.getRoot();
    }

    /**
     * Getter for property 'merkleTreeRoot'.
     *
     * @return Value for property 'merkleTreeRoot'.
     */
    public byte[] getMerkleTreeRoot() {
        return this.header.getMerkleTreeRoot();
    }

    /**
     * 获取Pow计数器
     *
     * @return Pow计数器
     */
    public long getNonce() {
        return this.header.getNonce();
    }

    /**
     * 获取区块高度
     *
     * @return 区块高度
     */
    public long getHeight() {
        return this.header.getHeight();
    }

    /**
     * 获取区块版本号
     *
     * @return 版本号
     */
    public byte[] getVersion() {
        return this.header.getVersion();
    }

    /**
     * Setter for property 'hash'.
     *
     * @param hash Value to set for property 'hash'.
     */
    void setHash(byte[] hash) {
        this.header.setHash(hash);
    }

    /**
     * Setter for property 'nonce'.
     *
     * @param nonce Value to set for property 'nonce'.
     */
    void setNonce(long nonce) {
        this.header.setNonce(nonce);
    }

    /**
     * Setter for property 'height'.
     *
     * @param height Value to set for property 'height'.
     */
    void setHeight(long height) {
        this.header.setHeight(height);
    }

}
