package com.shaoxia.rpccore.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author wjc28
 * @version 1.0
 * @description: Kryo 序列化器
 * @date 2024-06-26 15:12
 */
public class KryoSerializer implements Serializer{
	// 使用ThreadLocal来保证每一个线程有一个单独的Kryo实例
	private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(Kryo::new);

	/**
	 * 序列化对象
	 * @param object 要序列化的对象
	 * @param <T> 对象的类型
	 * @return 序列化后的字节数组
	 */
	@Override
	public <T> byte[] serialize(T object) {
		Kryo kryo = kryoThreadLocal.get();
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 Output output = new Output(byteArrayOutputStream)) {
			kryo.writeObject(output, object);
			output.close();
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("序列化失败", e);
		}
	}

	/**
	 * 反序列化对象
	 * @param bytes 序列化后的字节数组
	 * @param clazz 对象的类类型
	 * @param <T> 对象的类型
	 * @return 反序列化后的对象
	 */
	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		Kryo kryo = kryoThreadLocal.get();
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			 Input input = new Input(byteArrayInputStream)) {
			return kryo.readObject(input, clazz);
		} catch (Exception e) {
			throw new RuntimeException("反序列化失败", e);
		}
	}
}
