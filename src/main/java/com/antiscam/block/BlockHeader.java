package com.antiscam.block;

import com.antiscam.constant.Constant;
import com.antiscam.util.ByteUtil;

import java.io.UnsupportedEncodingException;

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
    /**
     * 区块高度
     */
    private long   height;
    /**
     * Merkle树根hash
     */
    private byte[] merkleTreeRoot;
    /**
     * 版本号
     */
    private byte[] version;

    private BlockHeader() {
    }

    /**
     * 构造块头信息
     *
     * @param previousHash 前一区块hash值
     * @param timestamp    时间戳
     */
    BlockHeader(byte[] previousHash, long timestamp) throws UnsupportedEncodingException {
        this.previousHash = ByteUtil.copy(previousHash);
        this.timestamp = timestamp;
        this.version = ByteUtil.toBytes(Constant.VERSION);
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

    /**
     * Getter for property 'merkleTreeRoot'.
     *
     * @return Value for property 'merkleTreeRoot'.
     */
    byte[] getMerkleTreeRoot() {
        return merkleTreeRoot;
    }

    /**
     * Setter for property 'merkleTreeRoot'.
     *
     * @param merkleTreeRoot Value to set for property 'merkleTreeRoot'.
     */
    void setMerkleTreeRoot(byte[] merkleTreeRoot) {
        this.merkleTreeRoot = merkleTreeRoot;
    }

    /**
     * Getter for property 'height'.
     *
     * @return Value for property 'height'.
     */
    public long getHeight() {
        return this.height;
    }

    /**
     * Setter for property 'height'.
     *
     * @param height Value to set for property 'height'.
     */
    public void setHeight(long height) {
        this.height = height;
    }

    /**
     * Getter for property 'version'.
     *
     * @return Value for property 'version'.
     */
    public byte[] getVersion() {
        return ByteUtil.copy(this.version);
    }
}
