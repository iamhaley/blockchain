package com.antiscam.network;

import com.antiscam.util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

/**
 * 通讯统一工具类
 */
class HandlerHelper {
    /**
     * 读取消息
     *
     * @param msg 消息
     * @throws UnsupportedEncodingException 异常
     */
    static void read(Object msg) throws UnsupportedEncodingException {
        ByteBuf buf     = (ByteBuf) msg;
        byte[]  byteMsg = new byte[buf.readableBytes()];
        buf.readBytes(byteMsg);

        String varMsg = ByteUtil.toString(byteMsg);
        System.out.println(varMsg);
        buf.release();
    }
}
