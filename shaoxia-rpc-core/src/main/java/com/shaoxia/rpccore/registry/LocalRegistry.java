package com.shaoxia.rpccore.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wjc28
 * @version 1.0
 * @description: 本地服务注册中心
 * @date 2024-06-23 21:24
 */
public class LocalRegistry {
	private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();

	/**
	 * 服务注册
	 * @param serviceName
	 * @param impClass
	 */
	public static void register(String serviceName,Class<?> impClass){
		map.put(serviceName,impClass);
	}

	/**
	 * 获取服务
	 * @param name
	 * @return
	 */
	public static Class<?> get(String name){
		return map.get(name);
	}

	/**
	 * 移除服务
	 * @param name
	 */
	public static void remove(String name){
		map.remove(name);
	}
}
