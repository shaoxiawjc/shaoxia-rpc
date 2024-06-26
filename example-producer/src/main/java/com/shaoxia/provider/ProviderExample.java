package com.shaoxia.provider;

import com.shaoxia.common.service.UserService;
import com.shaoxia.provider.service.impl.UserServiceImpl;
import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.registry.LocalRegistry;
import com.shaoxia.rpccore.server.HttpServer;
import com.shaoxia.rpccore.server.VertxHttpServer;

/**
 * @author wjc28
 * @version 1.0
 * @description: 示例提供
 * @date 2024-06-23 19:11
 */
public class ProviderExample {
	public static void main(String[] args) {
		RpcApplication.init();

		// 注册服务
		LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
		System.out.println("UserService Name: "+UserService.class.getName());

		HttpServer httpServer = new VertxHttpServer();
		httpServer.doPort(RpcApplication.getRpcConfig().getServerPort());
	}
}
