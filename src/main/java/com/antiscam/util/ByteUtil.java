package com.antiscam.util;

import com.antiscam.constant.Constant;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 字节工具
 */
public class ByteUtil {

    /**
     * 数组拷贝
     *
     * @param sourceBytes 源数组
     * @return 拷贝数组
     */
    public static byte[] copy(byte[] sourceBytes) {
        if (null == sourceBytes)
            return null;
        byte[] desBytes = new byte[sourceBytes.length];
        System.arraycopy(sourceBytes, 0, desBytes, 0, sourceBytes.length);
        return desBytes;
    }

    /**
     * 二维数组拷贝
     *
     * @param sourceBytes 源二维数组
     * @return 拷贝二维数组
     */
    public static byte[][] copy(byte[][] sourceBytes) {
        if (null == sourceBytes)
            return null;
        byte[][] desBytes = new byte[][]{};
        for (byte[] sourceByte : sourceBytes) {
            if (null == sourceByte)
                continue;
            desBytes = ArrayUtils.add(desBytes, sourceByte);
        }
        return desBytes;
    }

    /**
     * 将多个字节数组合并成一个字节数组
     *
     * @param bytesArray byte[]数组
     * @return 合并后字节数组
     */
    public static byte[] merge(byte[]... bytesArray) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (byte[] bytes : bytesArray) {
            if (null == bytes)
                continue;
            byteArrayOutputStream.write(bytes);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * byte[] 转 String
     *
     * @param bytes 字节数组
     * @return String
     */
    public static String toString(byte[] bytes) throws UnsupportedEncodingException {
        return null == bytes ? null : new String(bytes, Constant.ENCODING_UTF8);
    }

    /**
     * byte[] 转 16进制String
     *
     * @param bytes 字节数组
     * @return 16进制String
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder(bytes.length);
        String        temp;
        for (byte byt : bytes) {
            temp = Integer.toHexString(0xFF & byt);
            if (temp.length() < 2)
                stringBuilder.append(0);
            stringBuilder.append(temp.toUpperCase());
        }
        return stringBuilder.toString();
    }

    /**
     * String 转 byte[]
     *
     * @param value String值
     * @return byte[]
     */
    public static byte[] toBytes(String value) throws UnsupportedEncodingException {
        return value.getBytes(Constant.ENCODING_UTF8);
    }

    /**
     * long 转 byte[]
     *
     * @param value long值
     * @return byte[]
     */
    public static byte[] toBytes(long value) throws UnsupportedEncodingException {
        return String.valueOf(value).getBytes(Constant.ENCODING_UTF8);
    }
}
