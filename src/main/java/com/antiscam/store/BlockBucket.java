package com.antiscam.store;

/**
 * 区块桶
 *
 * @author wuming
 */
public class BlockBucket {
    /**
     * 区块桶前缀
     */
    public static final String BLOCKS_BUCKET_PREFIX = "blocks_";
    /**
     * 最新一个区块hash标识
     */
    public static final String LAST_BLOCK_HASH_KEY  = "l";

    private BlockBucket() {
    }
}
