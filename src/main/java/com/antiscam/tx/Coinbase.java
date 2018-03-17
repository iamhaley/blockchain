package com.antiscam.tx;

import com.antiscam.constant.Constant;

/**
 * 区块首笔交易
 *
 * @author wuming
 */
public class Coinbase extends Transaction {

    private Coinbase() {
    }

    /**
     * 构造首笔交易
     */
    public Coinbase(String toAddress, String data) {
        this.coinbase = true;
        this.txId = generateTxId();

        TxInput input = new TxInput(txId, -1, data);
        this.inputs = new TxInput[]{input};

        TxOutput output = new TxOutput(Constant.SUBSIDIES, toAddress);
        this.outputs = new TxOutput[]{output};
    }

}
