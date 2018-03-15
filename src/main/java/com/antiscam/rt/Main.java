package com.antiscam.rt;

import com.antiscam.core.Block;
import com.antiscam.core.Blockchain;
import com.antiscam.core.Pow;
import com.antiscam.util.ByteUtil;
import com.antiscam.util.DateUtil;

/**
 * 程式入口
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Blockchain blockchain = new Blockchain();
        blockchain.add("Send 1 BTC to wuming");
        blockchain.add("Send 2 more BTC to xiaokai");

        Blockchain.Itr iterator = blockchain.getIterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();

            System.out.println("Previous hash: " + ByteUtil.toString(block.getPreviousHash()));
            System.out.println("Data: " + block.getData());
            System.out.println("Time: " + DateUtil.longToDate(block.getTimestamp()));
            System.out.println("Hash: " + ByteUtil.toString(block.getHash()));
            System.out.println("Nonce: " + block.getNonce());
            System.out.println("Pow valid: " + new Pow(block).validate());
            System.out.println();
        }

    }
}
