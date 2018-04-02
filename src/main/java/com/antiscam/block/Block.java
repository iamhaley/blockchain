package com.antiscam.block;

import com.antiscam.tx.Transaction;
import com.antiscam.util.AssertUtil;
import com.antiscam.util.ByteUtil;
import org.apache.commons.codec.digest.DigestUtils;
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
     * 获取交易hash
     *
     * @return 交易hash
     * @throws IOException 异常
     */
    byte[] getTransactionsHash() throws IOException {
        AssertUtil.check(null != this.transactions && this.transactions.length > 0);

        byte[][] txIds = new byte[this.transactions.length][];
        for (Transaction transaction : this.transactions) {
            txIds = ArrayUtils.add(txIds, transaction.getTxId());
        }
        return ByteUtil.toBytes(DigestUtils.sha256Hex(ByteUtil.merge(txIds)));
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

}
