package com.antifraud.core;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 区块链结构
 *
 * @author wuming
 */
public class Blockchain {

    List<Block> blockchain = new LinkedList<>();

    /**
     * 构造区块链
     *
     * @throws IOException 异常
     */
    public Blockchain() throws IOException {
        blockchain.add(Block.getInstance(null, "Genesis block."));
    }

    /**
     * 添加块
     *
     * @param block 块信息
     */
    public void add(Block block) {
        assert null != blockchain && blockchain.size() > 0;

        this.blockchain.add(block);
    }

    /**
     * 添加块
     *
     * @param data 交易信息
     * @throws IOException 异常
     */
    public void add(String data) throws IOException {
        assert null != data && data.trim().length() > 0;

        Block previousBlock = blockchain.get(blockchain.size() - 1);
        this.add(Block.getInstance(previousBlock.getHash(), data));
    }

    /**
     * Getter for property 'blockchain'.
     *
     * @return Value for property 'blockchain'.
     */
    public List<Block> getBlockchain() {
        return blockchain;
    }
}
