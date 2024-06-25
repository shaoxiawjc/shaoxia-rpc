package com.shaoxia.rpceasy.proxy;

import java.lang.reflect.Proxy;

/**
 * @author wjc28
 * @version 1.0
 * @description: 服务代理工厂
 * @date 2024-06-25 12:28
 */
public class ServiceProxyFactory {
	public static <T> T getProxy(Class<T> serviceClass){
		System.out.println("getProxy: "+serviceClass);
		return (T) Proxy.newProxyInstance(
				serviceClass.getClassLoader(),
				new Class[]{serviceClass},
				new ServiceProxy());
	}
}
