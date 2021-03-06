package com.antiscam.rt;

import com.antiscam.block.Blockchain;
import com.antiscam.tx.Transaction;
import com.antiscam.tx.TxOutput;
import com.antiscam.wallet.WalletHandler;

/**
 * 客户端
 */
public class Client {

    public static void main(String[] args) throws Exception {
        for (String address : WalletHandler.getAllAddress()) {
            int balance = getBalance(address);
            System.out.println("coins of " + address + ": " + balance);
        }

//        send("12w3Z4Ap8pem424Z3vFxFbKSLzi13747GA", "1533LLk5c7xi59YVgQ651iTrWms692Rrtx", 15);
//        send("12w3Z4Ap8pem424Z3vFxFbKSLzi13747GA", "1Nj15kL5XRBg2Pe8ngP2b5ycn1CY4xkpWv", 10);
//        send("12w3Z4Ap8pem424Z3vFxFbKSLzi13747GA", "1JDzqRi7rPWyBdEpreR3mr76vDG42NvdoL", 6);
//        send("12w3Z4Ap8pem424Z3vFxFbKSLzi13747GA", "1AKgNhmsLqW5Kq9T8V8q9tQbGyKQ9fnJYZ", 1);
//        System.out.println("coins of 1JwWnLRLrpdoN2UgL8gP1kWyZMvuN4TBP9: " + wmBalance2);
//        System.out.println("coins of xiaokai: " + xkBalance2);
    }

    /**
     * 根据地址获取余额
     *
     * @param address 地址
     * @return 余额
     * @throws Exception 异常
     */
    private static int getBalance(String address) throws Exception {
        Blockchain blockchain       = new Blockchain(address);
        TxOutput[] unspentTxOutputs = blockchain.findUTXO(address);

        int balance = 0;
        for (TxOutput output : unspentTxOutputs) {
            balance += output.getValue();
        }
        return balance;
    }

    /**
     * 转账
     *
     * @param fromAddress 源地址
     * @param toAddress   目标地址
     * @param amount      金额
     */
    private static void send(String fromAddress, String toAddress, int amount) throws Exception {
        // 比特币转账会将新的交易记录存放在内存池中, 当矿工挖矿时, 打包内存中所有交易, 创建一个候选区块.
        // 当候选区块被成功挖出, 并添加到链中, 交易才被确认.
        // 实际创建交易后不回马上打包挖出区块, 此处简单将整个过程一起执行.
        Blockchain bc = new Blockchain(fromAddress);
        // 创建交易
        Transaction transaction = Transaction.buildUTXOTransaction(fromAddress, toAddress, amount, bc);
        // 打包交易, 挖矿
        bc.mine(new Transaction[]{transaction});

        System.out.println(fromAddress + " send " + amount + " coins to " + toAddress + " success");
    }

}
