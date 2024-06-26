package com.shaoxia.rpccore.serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wjc28
 * @version 1.0
 * @description: 序列化器工厂
 * @date 2024-06-26 15:19
 */
public class SerializerFactory {
	/**
	 * 序列化器映射
	 */
	private static final Map<String ,Serializer> KEY_SERIALIZER_MAP = new HashMap<String,Serializer>(){{
		put(SerializerKeys.JDK,new JdkSerializer());
		put(SerializerKeys.JSON,new JSONSerializer());
		put(SerializerKeys.KRYO,new KryoSerializer());
		put(SerializerKeys.HESSIAN,new HessianSerializer());
	}};

	/**
	 * 默认序列化器
	 */
	private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get(SerializerKeys.JDK);

	/**
	 * 获取实例
	 */
	public static Serializer getInstance(String key){
		return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
	}
}
