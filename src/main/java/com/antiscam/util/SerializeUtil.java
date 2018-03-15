package com.antiscam.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * 序列化工具
 */
public class SerializeUtil {

    /**
     * 反序列化
     *
     * @param bytes 对象对应的字节数组
     * @return Object
     */
    public static Object deserialize(byte[] bytes) {
        Input  input = new Input(bytes);
        Object obj   = new Kryo().readClassAndObject(input);
        input.close();
        return obj;
    }

    /**
     * 序列化
     *
     * @param object 需要序列化的对象
     * @return byte[]
     */
    public static byte[] serialize(Object object) {
        Output output = new Output(4096, -1);
        new Kryo().writeClassAndObject(output, object);
        byte[] bytes = output.toBytes();
        output.close();
        return bytes;
    }

    private SerializeUtil() {
    }
}
