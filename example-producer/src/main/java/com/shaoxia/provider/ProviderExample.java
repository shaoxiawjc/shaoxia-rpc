package com.shaoxia.provider;

import com.shaoxia.common.service.UserService;
import com.shaoxia.provider.service.impl.UserServiceImpl;
import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.config.RegistryConfig;
import com.shaoxia.rpccore.config.RpcConfig;
import com.shaoxia.rpccore.model.ServiceMetaInfo;
import com.shaoxia.rpccore.registry.LocalRegistry;
import com.shaoxia.rpccore.registry.Registry;
import com.shaoxia.rpccore.registry.RegistryFactory;
import com.shaoxia.rpccore.server.HttpServer;
import com.shaoxia.rpccore.server.VertxHttpServer;
import com.shaoxia.rpccore.server.tcp.VertxTcpServer;

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
		String serviceName = UserService.class.getName();
		LocalRegistry.register(serviceName, UserServiceImpl.class);
		System.out.println("Register Service Name: "+serviceName);

		// 注册服务到注册中心
		RpcConfig rpcConfig = RpcApplication.getRpcConfig();
		RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
		Registry re = RegistryFactory.getInstance(registryConfig.getRegistry());
		ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
		serviceMetaInfo.setServiceName(serviceName);
		serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
		serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
		try {
			re.register(serviceMetaInfo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		HttpServer httpServer = new VertxTcpServer();
		httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
	}
}
