package com.shaoxia;

import com.shaoxia.common.service.UserService;
import com.shaoxia.provider.service.impl.UserServiceImpl;
import com.shaoxia.rpceasy.registry.LocalRegistry;
import com.shaoxia.rpceasy.server.HttpServer;
import com.shaoxia.rpceasy.server.VertxHttpServer;

/**
 * @author wjc28
 * @version 1.0
 * @description: 示例提供
 * @date 2024-06-23 19:11
 */
public class ProviderExample {
	public static void main(String[] args) {
		// 注册服务
		LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
		System.out.println("UserService Name: "+UserService.class.getName());

		HttpServer httpServer = new VertxHttpServer();
		httpServer.doPort(8080);
	}
}
