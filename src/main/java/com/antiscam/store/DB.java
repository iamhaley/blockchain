package com.antiscam.store;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

/**
 * use an embedded key-value store from Facebook named RocksDb
 *
 * @author wuming
 */
class DB {

    /**
     * 静态内部类实现单例
     */
    private static class DBHolder {
        private static final String DB_FILE = "blockchain.db";
        private static RocksDB database;

        static {
            try {
                database = RocksDB.open(new Options().setCreateIfMissing(true), DB_FILE);
            } catch (RocksDBException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取数据库实例
     *
     * @return 数据库实例
     */
    static RocksDB getInstance() {
        return DBHolder.database;
    }

    private DB() {
    }
}
