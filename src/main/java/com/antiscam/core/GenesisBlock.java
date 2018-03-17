package com.antiscam.core;

import com.antiscam.tx.Coinbase;
import com.antiscam.tx.Transaction;

/**
 * 创世块
 *
 * @author wuming
 */
class GenesisBlock extends Block {

    private GenesisBlock() {
    }

    /**
     * 构造创世块
     *
     * @param coinbase 创世交易
     */
    private GenesisBlock(Coinbase coinbase) {
        long timestamp = System.currentTimeMillis();
        this.header = new BlockHeader(null, timestamp);
        this.transactions = new Transaction[]{coinbase};
    }

    /**
     * 获取创世块
     *
     * @param coinbase 创世交易
     * @return 创世块
     */
    static GenesisBlock getInstance(Coinbase coinbase) {
        return new GenesisBlock(coinbase);
    }

}
