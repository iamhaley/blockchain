package com.antiscam.store;

import com.antiscam.block.Block;
import com.antiscam.util.ByteUtil;
import com.antiscam.util.SerializeUtil;
import org.rocksdb.RocksDBException;

/**
 * 数据库处理器
 *
 * @author wuming
 */
public class DBHandler {

    /**
     * 存储最新一个区块的Hash值
     *
     * @param lastBlockHash 最新一个区块的Hash值
     * @throws RocksDBException 异常
     */
    public static void putLastBlockHash(String lastBlockHash) throws RocksDBException {
        byte[] key   = SerializeUtil.serialize(BlockBucket.BLOCKS_BUCKET_PREFIX + BlockBucket.LAST_BLOCK_HASH_KEY);
        byte[] value = SerializeUtil.serialize(lastBlockHash);
        DB.getInstance().put(key, value);
    }

    /**
     * 获取最新一个区块的Hash值
     *
     * @return 最新一个区块的Hash值
     * @throws Exception 异常
     */
    public static String getLastBlockHash() throws Exception {
        byte[] key                = SerializeUtil.serialize(BlockBucket.BLOCKS_BUCKET_PREFIX + BlockBucket.LAST_BLOCK_HASH_KEY);
        byte[] lastBlockHashBytes = DB.getInstance().get(key);
        return lastBlockHashBytes == null ? null : SerializeUtil.deserialize(lastBlockHashBytes).toString();
    }

    /**
     * 获取最新一个区块的高度
     *
     * @return 最新一个区块的高度
     * @throws Exception 异常
     */
    public static long getLastBlockHeight() throws Exception {
        byte[] key             = SerializeUtil.serialize(BlockBucket.BLOCKS_BUCKET_PREFIX + BlockBucket.LAST_BLOCK_HEIGHT_KEY);
        byte[] lastHeightBytes = DB.getInstance().get(key);
        return lastHeightBytes == null ? 0L : (long) SerializeUtil.deserialize(lastHeightBytes);
    }

    /**
     * 存储区块
     *
     * @param block 区块
     * @throws Exception 异常
     */
    public static void putBlock(Block block) throws Exception {
        byte[] key   = SerializeUtil.serialize(BlockBucket.BLOCKS_BUCKET_PREFIX + ByteUtil.toString(block.getHash()));
        byte[] value = SerializeUtil.serialize(block);
        DB.getInstance().put(key, value);

        String heightKey = BlockBucket.BLOCKS_BUCKET_PREFIX + BlockBucket.LAST_BLOCK_HEIGHT_KEY;
        synchronized (heightKey.intern()) {
            byte[] heightKeyBytes = SerializeUtil.serialize(heightKey);
            byte[] heightBytes    = DB.getInstance().get(heightKeyBytes);
            if (null == heightBytes) {
                DB.getInstance().put(heightKeyBytes, SerializeUtil.serialize(1L));
            } else {
                long height = (long) SerializeUtil.deserialize(heightBytes);
                DB.getInstance().put(heightKeyBytes, SerializeUtil.serialize(++height));
            }
        }
    }

    /**
     * 获取区块
     *
     * @param blockHash 区块Hash值
     * @return 区块
     * @throws RocksDBException 异常
     */
    public static Block getBlock(String blockHash) throws RocksDBException {
        byte[] key        = SerializeUtil.serialize(BlockBucket.BLOCKS_BUCKET_PREFIX + blockHash);
        byte[] blockBytes = DB.getInstance().get(key);
        return blockBytes == null ? null : (Block) SerializeUtil.deserialize(blockBytes);
    }

    /**
     * 关闭数据库
     */
    public static void close() {
        DB.getInstance().close();
    }

    private DBHandler() {
    }
}
