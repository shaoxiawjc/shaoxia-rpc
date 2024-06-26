package com.shaoxia.rpccore.proxy;

import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.config.RpcConfig;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

/**
 * @author wjc28
 * @version 1.0
 * @description: 服务代理工厂
 * @date 2024-06-25 12:28
 */
@Slf4j
public class ServiceProxyFactory {
	public static <T> T getProxy(Class<T> serviceClass){
		if (RpcApplication.getRpcConfig().isMock()){
			log.info("choose mock proxy");
			return getMockProxy(serviceClass);
		}
		return (T) Proxy.newProxyInstance(
				serviceClass.getClassLoader(),
				new Class[]{serviceClass},
				new ServiceProxy());
	}

	private static <T> T getMockProxy(Class<T> serviceClass) {
		return (T) Proxy.newProxyInstance(
				serviceClass.getClassLoader(),
				new Class[]{serviceClass},
				new MockServiceProxy());
	}
}
