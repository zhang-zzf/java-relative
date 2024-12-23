package com.github.zzf.dd.repo.redis.config;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-23
 */
public class KryoRedisSerializer implements RedisSerializer<Object> {
    /**
     * <pre>
     *
     * 由于 Kryo 不是线程安全的。 每个线程都应该有自己的 Kryo，Input 或 Output 实例。
     * 所以，使用 ThreadLocal 存放 Kryo 对象 这样减少了每次使用都实例化一次 Kryo 的开销又可以保证其线程安全
     * </pre>
     */
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);// 设置循环引用
        kryo.setRegistrationRequired(false);// 设置序列化时对象是否需要设置对象类型
        return kryo;
    });

    static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (o == null) {
            return EMPTY_BYTE_ARRAY;
        }
        try (Output output = new Output(4096)) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            kryo.writeClassAndObject(output, o);
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializationException("Could not serialize object to Kryo", e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (Input input = new Input(bytes)) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            Object o = kryo.readClassAndObject(input);
            return o;
        } catch (Exception e) {
            throw new SerializationException("Could not deserialize object from Kryo", e);
        }
    }
}
