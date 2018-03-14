package com.antiscam.core;

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
    private BlockHeader header;
    /**
     * 数据
     */
    private String      data;

    /**
     * 构造块
     *
     * @param header 头信息
     * @param data   交易信息
     */
    private Block(BlockHeader header, String data) {
        this.header = header;
        this.data = data;
    }

    /**
     * * 获取普通块
     *
     * @param previousHash 前一区块hash值
     * @param data         交易信息
     * @return 普通块
     * @throws IOException 异常
     */
    public static Block getInstance(byte[] previousHash, String data) throws IOException {
        long  timestamp = System.currentTimeMillis();
        Block block     = new Block(new BlockHeader(previousHash, timestamp), data);

        Pow pow = new Pow(block);
        pow.caculate();

        return block;
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
     * Getter for property 'data'.
     *
     * @return Value for property 'data'.
     */
    public String getData() {
        return data;
    }

    public long getNonce() {
        return this.header.getNonce();
    }

    /**
     * Setter for property 'hash'.
     *
     * @param hash Value to set for property 'hash'.
     */
    public void setHash(byte[] hash) {
        this.header.setHash(hash);
    }

    /**
     * Setter for property 'nonce'.
     *
     * @param nonce Value to set for property 'nonce'.
     */
    public void setNonce(long nonce) {
        this.header.setNonce(nonce);
    }

}
