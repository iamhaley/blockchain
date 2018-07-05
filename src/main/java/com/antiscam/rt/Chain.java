package com.antiscam.rt;

import com.antiscam.block.Block;
import com.antiscam.block.Blockchain;
import com.antiscam.block.Pow;
import com.antiscam.util.Base58Util;
import com.antiscam.util.ByteUtil;
import com.antiscam.util.DateUtil;
import com.antiscam.wallet.Wallet;
import com.antiscam.wallet.WalletHandler;
import org.apache.commons.lang3.ArrayUtils;

public class Chain {

    public static void main(String[] args) throws Exception {

//        Wallet wallet = WalletHandler.getWallet();
//        System.out.println("wallet address: " + wallet.getAddress());
//        Blockchain blockchain = new Blockchain(wallet.getAddress());

        Blockchain blockchain = new Blockchain();

        Blockchain.Itr iterator = blockchain.getIterator();
        System.out.print("[");
        while (iterator.hasNext()) {
            Block block = iterator.next();
            System.out.print("{");
            System.out.println("\"Version\": \"" + ByteUtil.toString(block.getVersion()) + "\",");
            System.out.println("\"Previous hash\": \"" + ByteUtil.toString(block.getPreviousHash()) + "\",");
            System.out.println("\"Transaction\": " + ArrayUtils.toString(block.getTransactions()) + ",");
            System.out.println("\"Time\": \"" + DateUtil.longToString(block.getTimestamp()) + "\",");
            System.out.println("\"Hash\": \"" + ByteUtil.toString(block.getHash()) + "\",");
            System.out.println("\"Nonce\": " + block.getNonce() + ",");
            System.out.println("\"Height\": " + block.getHeight() + ",");
            System.out.println("\"MerkleTreeRoot\": \"" + Base58Util.encode(block.getMerkleTreeRoot()) + "\",");
            System.out.println("\"Pow valid\": " + new Pow(block).validate());
            System.out.print("}");
            if (iterator.hasNext())
                System.out.print(",");
        }
        System.out.print("]");
    }

}
