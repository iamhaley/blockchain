package com.antiscam.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author wuming
 */
public class FileUtil {

    /**
     * 写文件
     *
     * @param path      文件路径
     * @param dataBytes 内容字节序列
     * @return 文件路径
     */
    public static String write(String path, byte[] dataBytes) {
        File file = new File(path);
        write(file, dataBytes);
        return path;
    }

    /**
     * 写文件
     *
     * @param file      文件
     * @param dataBytes 内容字节序列
     */
    private static void write(File file, byte[] dataBytes) {
        try {
            FileUtils.writeByteArrayToFile(file, dataBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读文件
     *
     * @param path 文件路径
     * @return 文件字节序列
     */
    public static byte[] read(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return read(file);
    }

    /**
     * 读文件
     *
     * @param file 文件
     * @return 文件字节序列
     */
    private static byte[] read(File file) {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private FileUtil() {
    }
}
