package com.shaoxia.rpceasy.serializer;

import java.io.IOException;

/**
 * @author wjc28
 * @version 1.0
 * @description: 序列化接口
 * @date 2024-06-23 23:10
 */
public interface Serializer {
	/**
	 * 序列化
	 * @param object
	 * @return
	 * @param <T>
	 * @throws IOException
	 */
	<T> byte[] serialize(T object) throws IOException;

	/**
	 * 反序列化
	 * @param bytes
	 * @param type
	 * @return
	 * @param <T>
	 * @throws IOException
	 */
	<T> T deserialize(byte[] bytes,Class<T> type) throws IOException;
}
