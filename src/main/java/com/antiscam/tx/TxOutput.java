package com.antiscam.tx;

/**
 * 交易输出
 *
 * @author wuming
 */
public class TxOutput {

    /**
     * 价值
     */
    private int    value;
    /**
     * 锁定脚本
     */
    private String scriptPubKey;

    private TxOutput() {
    }

    /**
     * 构造交易输出
     *
     * @param value        价值
     * @param scriptPubKey 锁定脚本
     */
    TxOutput(int value, String scriptPubKey) {
        this.value = value;
        this.scriptPubKey = scriptPubKey;
    }

    /**
     * Getter for property 'value'.
     *
     * @return Value for property 'value'.
     */
    public int getValue() {
        return value;
    }

    /**
     * 是否可以解锁交易输出
     *
     * @param unlockingData
     * @return true: 可以, false: 不可以
     */
    public boolean canBeUnlockedWith(String unlockingData) {
        return this.scriptPubKey.endsWith(unlockingData);
    }

    @Override
    public String toString() {
        return "value:" + value + ",scriptPubKey:" + scriptPubKey;
    }
}
