package com.antiscam.tx;

import java.util.Map;

/**
 * 账单
 *
 * @author wuming
 */
public class Bill {

    /**
     * 总额
     */
    private int                accumulated;
    /**
     * 交易输出索引映射
     * <p>
     * 交易Id --> 未花费输出索引集
     * </p>
     */
    private Map<String, int[]> outputsMap;

    /**
     * 构造账单
     *
     * @param accumulated 总额
     * @param outputsMap  交易输出索引映射
     */
    public Bill(int accumulated, Map<String, int[]> outputsMap) {
        this.accumulated = accumulated;
        this.outputsMap = outputsMap;
    }

    /**
     * Getter for property 'accumulated'.
     *
     * @return Value for property 'accumulated'.
     */
     int getAccumulated() {
        return accumulated;
    }

    /**
     * Getter for property 'outputsMap'.
     *
     * @return Value for property 'outputsMap'.
     */
     Map<String, int[]> getOutputsMap() {
        return outputsMap;
    }
}
