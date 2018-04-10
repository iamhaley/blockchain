package com.antiscam.tx;

import com.antiscam.constant.Constant;
import com.antiscam.wallet.Wallet;
import com.antiscam.wallet.WalletHandler;

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
    public Coinbase(String toAddress) {
        this.coinbase = true;
        this.txId = generateTxId();

        Wallet wallet = WalletHandler.getWallet(toAddress);

        TxInput input = new TxInput(txId, -1, null, wallet.getUncompressedPublicKey());
        this.inputs = new TxInput[]{input};

        TxOutput output = new TxOutput(Constant.SUBSIDIES, toAddress);
        this.outputs = new TxOutput[]{output};
    }

}
