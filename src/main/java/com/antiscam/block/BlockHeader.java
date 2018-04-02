package com.antiscam.block;

import com.antiscam.util.ByteUtil;

/**
 * 块头结构
 *
 * @author wuming
 */
class BlockHeader {
    /**
     * 当前hash值
     */
    private byte[] hash;
    /**
     * 前一区块hash值
     */
    private byte[] previousHash;
    /**
     * 时间戳
     */
    private long   timestamp;
    /**
     * Pow计数器
     */
    private long   nonce;

    private BlockHeader() {
    }

    /**
     * 构造块头信息
     *
     * @param previousHash 前一区块hash值
     * @param timestamp    时间戳
     */
    BlockHeader(byte[] previousHash, long timestamp) {
        this.previousHash = ByteUtil.copy(previousHash);
        this.timestamp = timestamp;
    }

    /**
     * Getter for property 'hash'.
     *
     * @return Value for property 'hash'.
     */
    byte[] getHash() {
        return ByteUtil.copy(hash);
    }

    /**
     * Getter for property 'previousHash'.
     *
     * @return Value for property 'previousHash'.
     */
    byte[] getPreviousHash() {
        return ByteUtil.copy(previousHash);
    }

    /**
     * Getter for property 'timestamp'.
     *
     * @return Value for property 'timestamp'.
     */
    long getTimestamp() {
        return timestamp;
    }

    /**
     * Setter for property 'hash'.
     *
     * @param hash Value to set for property 'hash'.
     */
    void setHash(byte[] hash) {
        this.hash = ByteUtil.copy(hash);
    }

    /**
     * Getter for property 'nonce'.
     *
     * @return Value for property 'nonce'.
     */
    long getNonce() {
        return nonce;
    }

    /**
     * Setter for property 'nonce'.
     *
     * @param nonce Value to set for property 'nonce'.
     */
    void setNonce(long nonce) {
        this.nonce = nonce;
    }
}
