package com.antiscam.util;

import com.antiscam.wallet.Wallet;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

/**
 * 基于 Kryo 序列化工具
 */
public class SerializeUtil {

    private static final Kryo kryo = new Kryo();

    static {
        kryo.register(Wallet.class, new JavaSerializer());
    }

    /**
     * 反序列化
     *
     * @param bytes 对象对应的字节数组
     * @return Object
     */
    public static Object deserialize(byte[] bytes) {
        Input  input = new Input(bytes);
        Object obj   = kryo.readClassAndObject(input);
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
        kryo.writeClassAndObject(output, object);
        byte[] bytes = output.toBytes();
        output.close();
        return bytes;
    }

    private SerializeUtil() {
    }
}
