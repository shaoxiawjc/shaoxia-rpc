package com.shaoxia.rpccore.registry;

import com.shaoxia.rpccore.spi.SpiLoader;

/**
 * @author wjc28
 * @version 1.0
 * @description: 注册中心工厂类
 * @date 2024-06-28 13:26
 */
public class RegistryFactory {
	static {
		SpiLoader.load(Registry.class);
	}

	/**
	 * 默认注册中心
	 */
	private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

	public static Registry getInstance(String key){
		return SpiLoader.getInstance(Registry.class,key);
	}
}
