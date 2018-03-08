package com.antifraud.rt;

import com.antifraud.core.Block;
import com.antifraud.core.Blockchain;
import com.antifraud.core.Pow;
import com.antifraud.util.ByteUtil;
import com.antifraud.util.DateUtil;

import java.io.IOException;
import java.text.ParseException;

/**
 * 程式入口
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Blockchain blockchain = new Blockchain();
        blockchain.add("Send 1 BTC to wuming");
        blockchain.add("Send 2 more BTC to xiaokai");

        for (Block block : blockchain.getBlockchain()) {
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
