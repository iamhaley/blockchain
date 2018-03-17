package com.antiscam.store;

/**
 * 区块桶
 *
 * @author wuming
 */
class BlockBucket {
    /**
     * 区块桶前缀
     */
    static final String BLOCKS_BUCKET_PREFIX = "blocks_";
    /**
     * 最新一个区块hash标识
     */
    static final String LAST_BLOCK_HASH_KEY  = "l";

    private BlockBucket() {
    }
}
