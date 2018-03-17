package com.antiscam.core;

import com.antiscam.constant.Constant;
import com.antiscam.util.ByteUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 工作量证明
 *
 * @author wuming
 */
public class Pow {
    /**
     * 难度
     */
    private Difficulty difficulty;
    /**
     * 块
     */
    private Block      block;

    /**
     * 构造工作量证明
     *
     * @param block 块信息
     */
    public Pow(Block block) {
        this.difficulty = new Difficulty();
        this.block = block;
    }

    /**
     * 计算符合目标的hash
     *
     * @throws IOException 异常
     */
    void calculate() throws IOException {
        long nonce = 0;

        while (nonce < Long.MAX_VALUE) {
            String hash = getBlockHash(nonce);
            if (new BigInteger(hash, Constant.RADIX_HEX).compareTo(this.difficulty.getTarget()) < 0) {
                this.block.setHash(ByteUtil.toBytes(hash));
                this.block.setNonce(nonce);
                break;
            } else {
                System.out.printf("%s not match, throw away.", hash);
                System.out.println();
                nonce++;
            }
        }
    }

    /**
     * 区块有效性验证
     *
     * @return true: 有效, false: 无效
     * @throws IOException 异常
     */
    public boolean validate() throws IOException {
        String blockHash = getBlockHash(this.block.getNonce());
        return new BigInteger(blockHash, Constant.RADIX_HEX).compareTo(this.difficulty.getTarget()) < 0;
    }

    /**
     * 获取包括目标位与计数的区块Hash
     *
     * @param nonce 计数器
     * @return 包括目标位与计数的区块Hash
     * @throws IOException 异常
     */
    private String getBlockHash(long nonce) throws IOException {
        return DigestUtils.sha256Hex(ByteUtil.merge(
                this.block.getPreviousHash(),
                this.block.getTransactionsHash(),
                ByteUtil.toBytes(this.block.getTimestamp()),
                ByteUtil.toBytes(Difficulty.targetBit),
                ByteUtil.toBytes(nonce)
        ));
    }

}