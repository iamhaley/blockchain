package com.antiscam.rt;

import com.antiscam.block.Block;
import com.antiscam.block.Blockchain;
import com.antiscam.block.Pow;
import com.antiscam.util.ByteUtil;
import com.antiscam.util.DateUtil;
import com.antiscam.wallet.Wallet;
import com.antiscam.wallet.WalletHandler;
import org.apache.commons.lang3.ArrayUtils;

public class Chain {

    public static void main(String[] args) throws Exception {

        Wallet wallet = WalletHandler.getWallet();
        System.out.println("wallet address: " + wallet.getAddress());

        Blockchain blockchain = new Blockchain(wallet.getAddress());

        Blockchain.Itr iterator = blockchain.getIterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();

            System.out.println("Previous hash: " + ByteUtil.toString(block.getPreviousHash()));
            System.out.println(ArrayUtils.toString(block.getTransactions()));
            System.out.println("Time: " + DateUtil.longToDate(block.getTimestamp()));
            System.out.println("Hash: " + ByteUtil.toString(block.getHash()));
            System.out.println("MerkleTreeRoot: " + ByteUtil.toString(block.getMerkleTreeRoot()));
            System.out.println("Nonce: " + block.getNonce());
            System.out.println("Pow valid: " + new Pow(block).validate());
            System.out.println();
        }

    }

}
