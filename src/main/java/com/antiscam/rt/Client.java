package com.antiscam.rt;

import com.antiscam.core.Blockchain;
import com.antiscam.tx.Transaction;
import com.antiscam.tx.TxOutput;

/**
 * 客户端
 */
public class Client {

    public static void main(String[] args) throws Exception {
        int wmBalance = getBalance("wuming");
        int xkBalance = getBalance("xiaokai");
        System.out.println("coins of wuming: " + wmBalance);
        System.out.println("coins of xiaokai: " + xkBalance);

//        send("wuming", "xiaokai", 10);
//        int wmBalance2 = getBalance("wuming");
//        int xkBalance2 = getBalance("xiaokai");
//        System.out.println("coins of wuming: " + wmBalance2);
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
