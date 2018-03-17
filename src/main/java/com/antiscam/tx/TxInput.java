package com.antiscam.tx;

import com.antiscam.util.ByteUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;

/**
 * 交易输入
 *
 * @author wuming
 */
public class TxInput {

    /**
     * 交易Id
     */
    private byte[] txId;
    /**
     * 交易输出索引
     */
    private int    txOutputIndex;
    /**
     * 解锁脚本
     */
    private String scriptSig;

    private TxInput() {
    }

    /**
     * 构造交易输入
     *
     * @param txId          交易Id
     * @param txOutputIndex 交易输出索引
     * @param scriptSig     解锁脚本
     */
    TxInput(byte[] txId, int txOutputIndex, String scriptSig) {
        this.txId = txId;
        this.txOutputIndex = txOutputIndex;
        this.scriptSig = scriptSig;
    }

    /**
     * Getter for property 'txId'.
     *
     * @return Value for property 'txId'.
     */
    public byte[] getTxId() {
        return ArrayUtils.clone(txId);
    }

    /**
     * Getter for property 'txOutputIndex'.
     *
     * @return Value for property 'txOutputIndex'.
     */
    public int getTxOutputIndex() {
        return txOutputIndex;
    }

    /**
     * 是否可以解锁交易输出
     *
     * @param unlockingData
     * @return true: 可以, false: 不可以
     */
    public boolean canUnlockOutputWith(String unlockingData) {
        return this.scriptSig.endsWith(unlockingData);
    }

    @Override
    public String toString() {
        try {
            return "txId:" + ByteUtil.toString(txId) + ",txOutputIndex:" + txOutputIndex + ",scriptSig:" + scriptSig;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return super.toString();
        }
    }
}
