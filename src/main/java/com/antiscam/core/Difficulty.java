package com.antiscam.core;

import java.math.BigInteger;

/**
 * Pow难度
 *
 * @author wuming
 */
public class Difficulty {
    /**
     * 目标位
     */
    public static final int targetBit = 20;
    /**
     * 目标值
     */
    private BigInteger target;

    /**
     * 构造Pow难度
     * <p>
     * 对1进行移位运算，将1向左移动 (256 - targetBit) 位，得到难度目标值
     */
    public Difficulty() {
        this.target = BigInteger.valueOf(1).shiftLeft((256 - Difficulty.targetBit));
    }

    /**
     * Getter for property 'target'.
     *
     * @return Value for property 'target'.
     */
    public BigInteger getTarget() {
        return target;
    }
}
