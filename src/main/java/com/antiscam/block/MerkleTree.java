package com.antiscam.block;

import com.antiscam.util.ByteUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

/**
 * 默克尔树
 * 当叶子节点为奇数时, 采取不拷贝方式, 孤儿左节点自成父节点
 */
public class MerkleTree {

    /**
     * 所有区块交易hash集合
     */
    private byte[][] txHashes;
    /**
     * 根
     */
    private byte[] root = new byte[]{};

    /**
     * 构造方法
     *
     * @param txHashes
     */
    MerkleTree(byte[][] txHashes) {
        this.txHashes = txHashes;
    }

    /**
     * 创建默克尔树
     *
     * @throws IOException 异常
     */
    void buildTree() throws IOException {
        byte[][] newTxHashes = ByteUtil.copy(this.txHashes);
        while (newTxHashes.length != 1)
            newTxHashes = getNewTxHashes(newTxHashes);
        this.root = newTxHashes[0];
    }

    /**
     * 构建叶子hash节点的父hash节点集合
     *
     * @param txHashes 叶子hash节点
     * @return 父hash节点集合
     * @throws IOException 异常
     */
    private byte[][] getNewTxHashes(byte[][] txHashes) throws IOException {
        byte[][] newTxHashes = new byte[txHashes.length][];
        int      index       = 0;
        while (index < txHashes.length) {
            byte[] leftTxHash = txHashes[index];
            index++;
            byte[] rightTxHash = new byte[]{};
            if (index < txHashes.length)
                rightTxHash = txHashes[index];
            byte[] parentHash = ByteUtil.toBytes(DigestUtils.sha256Hex(ByteUtil.merge(leftTxHash, rightTxHash)));
            newTxHashes = ArrayUtils.add(txHashes, parentHash);
            index++;
        }
        return newTxHashes;
    }

    /**
     * Getter for property 'root'.
     *
     * @return Value for property 'root'.
     */
    public byte[] getRoot() {
        return root;
    }
}
