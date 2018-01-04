package com.rpc.common.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化工具
 * @Author gu
 * @Version V1.0
 * @Date 17/12/29 下午4:34
 */
public class SerializationUtil {
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
    private static Objenesis objenesis = new ObjenesisStd(true);

    private SerializationUtil(){}

    public static <T> byte[] serialize(T obj){
        LinkedBuffer buffer = null;
        try {
            buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
            Schema schema = getSchema(obj.getClass());
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        }catch (Exception e){
            throw e;
        }finally {
            if (buffer != null) {
                buffer.clear();
            }
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> clazz){
        try {
            T message = objenesis.newInstance(clazz);
            Schema schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        }catch (Exception e){
            throw e;
        }
    }

    private static <T> Schema getSchema(Class<T> clazz){
        Schema schema = cachedSchema.get(clazz);
        if(schema == null){
            schema = RuntimeSchema.createFrom(clazz);
            cachedSchema.put(clazz, schema);
        }

        return schema;
    }
}
