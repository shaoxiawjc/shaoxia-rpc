package com.shaoxia.rpccore.serializer;

import com.shaoxia.rpccore.spi.SpiLoader;
import lombok.extern.slf4j.Slf4j;


/**
 * @author wjc28
 * @version 1.0
 * @description: 序列化器工厂
 * @date 2024-06-26 15:19
 */
@Slf4j
public class SerializerFactory {

	static {
		SpiLoader.load(Serializer.class);
	}


	/**
	 * 默认序列化器
	 */
	private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

	/**
	 * 获取实例
	 */
	public static Serializer getInstance(String key){
		return SpiLoader.getInstance(Serializer.class,key);
	}
}
