package com.shaoxia.rpccore.bootstrap;

import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.config.RegistryConfig;
import com.shaoxia.rpccore.config.RpcConfig;
import com.shaoxia.rpccore.model.ServiceMetaInfo;
import com.shaoxia.rpccore.model.ServiceRegisterInfo;
import com.shaoxia.rpccore.registry.LocalRegistry;
import com.shaoxia.rpccore.registry.Registry;
import com.shaoxia.rpccore.registry.RegistryFactory;
import com.shaoxia.rpccore.server.HttpServer;
import com.shaoxia.rpccore.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * @author wjc28
 * @version 1.0
 * @description: 服务提供者启动对象
 * @date 2024-07-07 16:33
 */
public class ProviderBootstrap {
	public static void init(List<ServiceRegisterInfo> serviceRegisterInfos){
		// RPC框架的初始化
		RpcApplication.init();

		final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

		RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
		for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfos) {
			// 注册服务到注册中心
			String serviceName = serviceRegisterInfo.getServiceName();
			Class<?> impClass = serviceRegisterInfo.getImpClass();
			LocalRegistry.register(serviceName,impClass);
			Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
			ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
			serviceMetaInfo.setServiceName(serviceName);
			serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
			serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
			try {
				registry.register(serviceMetaInfo);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		HttpServer httpServer = new VertxTcpServer();
		httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
	}
}
