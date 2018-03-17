package com.antiscam.core;

import com.antiscam.store.DBHandler;
import com.antiscam.tx.*;
import com.antiscam.util.ByteUtil;
import com.sun.tools.javac.util.Assert;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 区块链结构
 *
 * @author wuming
 */
public class Blockchain {
    /**
     * 最新区块hash
     */
    private String lastBlockHash;

    /**
     * 构造区块链
     *
     * @throws IOException 异常
     */
    public Blockchain(String address) throws Exception {
        String lastBlockHash = DBHandler.getLastBlockHash();

        if (null == lastBlockHash || lastBlockHash.trim().length() == 0) {
            Coinbase     coinbase     = new Coinbase(address, "Coinbase transaction");
            GenesisBlock genesisBlock = GenesisBlock.getInstance(coinbase);
            add(genesisBlock);
        }

        this.lastBlockHash = lastBlockHash;
    }

    /**
     * 添加块
     *
     * @param block 块信息
     */
    private void add(Block block) throws Exception {
        Pow pow = new Pow(block);
        pow.calculate();

        DBHandler.putBlock(block);
        DBHandler.putLastBlockHash(ByteUtil.toString(block.getHash()));

        this.lastBlockHash = ByteUtil.toString(block.getHash());
    }

    /**
     * 交易打包, 挖矿
     *
     * @param transactions 交易
     * @throws IOException 异常
     */
    public void mine(Transaction[] transactions) throws Exception {
        Assert.check(null != transactions && transactions.length > 0);

        String lastBlockHash = DBHandler.getLastBlockHash();
        Assert.check(null != lastBlockHash && lastBlockHash.trim().length() > 0);

        Block block = Block.getInstance(ByteUtil.toBytes(lastBlockHash), transactions);
        this.add(block);
    }

    /**
     * 根据地址查找所有未花费交易输出
     *
     * @param address 地址
     * @return 所有未花费交易输出
     */
    public TxOutput[] findUTXO(String address) {
        Transaction[] unspentTransactions = findUnspentTransactions(address);
        TxOutput[]    unspentTxOutputs    = {};

        if (unspentTransactions.length == 0) {
            return unspentTxOutputs;
        }

        for (Transaction transaction : unspentTransactions) {
            for (TxOutput output : transaction.getOutputs()) {
                if (output.canBeUnlockedWith(address)) {
                    unspentTxOutputs = ArrayUtils.add(unspentTxOutputs, output);
                }
            }
        }

        return unspentTxOutputs;
    }

    /**
     * 根据地址查找所有未花费交易
     *
     * @param address 地址
     * @return 所有未花费交易
     */
    private Transaction[] findUnspentTransactions(String address) {
        Transaction[] unspentTransactions = {};

        Map<String, int[]> spentTxOutputIndexMap = findSpentTransactionOutputs(address);

        Blockchain.Itr iterator = getIterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            for (Transaction transaction : block.getTransactions()) {
                String txIdHexStr           = Hex.encodeHexString(transaction.getTxId());
                int[]  spentTxOutputIndexes = spentTxOutputIndexMap.get(txIdHexStr);

                for (int txOutputIndex = 0; txOutputIndex < transaction.getOutputs().length; txOutputIndex++) {
                    if (null != spentTxOutputIndexes && ArrayUtils.contains(spentTxOutputIndexes, txOutputIndex)) {
                        continue;
                    }
                    if (transaction.getOutputs()[txOutputIndex].canBeUnlockedWith(address)) {
                        unspentTransactions = ArrayUtils.add(unspentTransactions, transaction);
                    }
                }
            }
        }

        return unspentTransactions;
    }

    /**
     * 根据地址查找所有已花费输出
     *
     * @param address 地址
     * @return 所有已花费输出
     */
    private Map<String, int[]> findSpentTransactionOutputs(String address) {
        // 交易Id --> 已花费输出索引集
        Map<String, int[]> spentTxOutputIndexMap = new HashMap<>();

        Blockchain.Itr iterator = getIterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            for (Transaction transaction : block.getTransactions()) {
                if (transaction.isCoinbase()) {
                    continue;
                }
                for (TxInput input : transaction.getInputs()) {
                    if (!input.canUnlockOutputWith(address)) {
                        continue;
                    }
                    String inputTxIdHexStr    = Hex.encodeHexString(input.getTxId());
                    int[]  spentOutputIndexes = spentTxOutputIndexMap.get(inputTxIdHexStr);
                    if (null == spentOutputIndexes) {
                        spentTxOutputIndexMap.put(inputTxIdHexStr, new int[]{input.getTxOutputIndex()});
                    } else {
                        int[] txOutputIndexes = ArrayUtils.add(spentOutputIndexes, input.getTxOutputIndex());
                        spentTxOutputIndexMap.put(inputTxIdHexStr, txOutputIndexes);
                    }
                }
            }
        }

        return spentTxOutputIndexMap;
    }

    /**
     * 构造账单
     *
     * @param address 地址
     * @param amount  价值
     * @return 账单
     */
    public Bill buildBill(String address, int amount) {
        Transaction[] unspentTransactions = findUnspentTransactions(address);

        // 账单总额
        int accumulated = 0;
        // 未花费输出索引映射
        Map<String, int[]> outputsMap = new HashMap<>();

        for (Transaction transaction : unspentTransactions) {
            String txIdHexStr = Hex.encodeHexString(transaction.getTxId());

            for (int txOutputIndex = 0; txOutputIndex < transaction.getOutputs().length; txOutputIndex++) {
                TxOutput output = transaction.getOutputs()[txOutputIndex];
                if (output.canBeUnlockedWith(address) && accumulated < amount) {
                    accumulated += output.getValue();

                    int[] outputs = outputsMap.get(txIdHexStr);
                    if (null == outputs) {
                        outputsMap.put(txIdHexStr, new int[]{txOutputIndex});
                    } else {
                        outputs = ArrayUtils.add(outputs, txOutputIndex);
                        outputsMap.put(txIdHexStr, outputs);
                    }

                    if (accumulated >= amount) {
                        break;
                    }
                }
            }
        }

        return new Bill(accumulated, outputsMap);
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
