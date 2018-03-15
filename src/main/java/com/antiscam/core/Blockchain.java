package com.antiscam.core;

import com.antiscam.store.DBHandler;
import com.antiscam.util.ByteUtil;

import java.io.IOException;
import java.util.Iterator;

/**
 * 区块链结构
 *
 * @author wuming
 */
public class Blockchain {
    /**
     * 最新区块hash值
     */
    private String lastBlockHash;

    /**
     * 构造区块链
     *
     * @throws IOException 异常
     */
    public Blockchain() throws Exception {
        String lastBlockHash = DBHandler.getLastBlockHash();

        if (null == lastBlockHash || lastBlockHash.trim().length() == 0) {
            Block genesisBlock = Block.getInstance(null, "Genesis block");
            DBHandler.putBlock(genesisBlock);
            DBHandler.putLastBlockHash(ByteUtil.toString(genesisBlock.getHash()));
            lastBlockHash = ByteUtil.toString(genesisBlock.getHash());
        }

        this.lastBlockHash = lastBlockHash;
    }

    /**
     * 添加块
     *
     * @param block 块信息
     */
    public void add(Block block) throws Exception {
        DBHandler.putBlock(block);
        DBHandler.putLastBlockHash(ByteUtil.toString(block.getHash()));

        this.lastBlockHash = ByteUtil.toString(block.getHash());
    }

    /**
     * 添加块
     *
     * @param data 交易信息
     * @throws IOException 异常
     */
    public void add(String data) throws Exception {
        assert null != data && data.trim().length() > 0;

        String lastBlockHash = DBHandler.getLastBlockHash();
        assert null != lastBlockHash && lastBlockHash.trim().length() > 0;

        Block block = Block.getInstance(ByteUtil.toBytes(lastBlockHash), data);
        this.add(block);
    }

    /**
     * Getter for property 'lastBlockHash'.
     *
     * @return Value for property 'lastBlockHash'.
     */
    public String getLastBlockHash() {
        return lastBlockHash;
    }

    /**
     * 获取迭代器
     *
     * @return 迭代器
     */
    public Itr getIterator() {
        return new Itr(this.lastBlockHash);
    }

    /**
     * 迭代器
     * <p>
     * 从当前区块逆序回溯至创世区块
     * </p>
     */
    public static class Itr implements Iterator<Block> {
        /**
         * 当前区块hash
         */
        private String currentBlockHash;

        private Itr(String currentBlockHash) {
            this.currentBlockHash = currentBlockHash;
        }

        @Override
        public boolean hasNext() {
            if (null == this.currentBlockHash || this.currentBlockHash.trim().length() == 0) {
                return false;
            }
            try {
                Block currentBlock = DBHandler.getBlock(this.currentBlockHash);
                // if(区块非空 && (创世区块 || 前一区块非空)) return true
                return null != currentBlock && (null == currentBlock.getPreviousHash() || null != DBHandler.getBlock(ByteUtil.toString(currentBlock.getPreviousHash())));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public Block next() {
            try {
                Block currentBlock = DBHandler.getBlock(this.currentBlockHash);
                if (null != currentBlock) {
                    this.currentBlockHash = ByteUtil.toString(currentBlock.getPreviousHash());
                    return currentBlock;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
