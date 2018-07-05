package com.antiscam.block;

import com.antiscam.tx.Coinbase;
import com.antiscam.tx.Transaction;

import java.io.UnsupportedEncodingException;

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
    private GenesisBlock(Coinbase coinbase) throws UnsupportedEncodingException {
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
    static GenesisBlock getInstance(Coinbase coinbase) throws UnsupportedEncodingException {
        return new GenesisBlock(coinbase);
    }

}
