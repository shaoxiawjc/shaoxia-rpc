package com.shaoxia.provider;

import com.shaoxia.common.service.UserService;
import com.shaoxia.provider.service.impl.UserServiceImpl;
import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.bootstrap.ProviderBootstrap;
import com.shaoxia.rpccore.config.RegistryConfig;
import com.shaoxia.rpccore.config.RpcConfig;
import com.shaoxia.rpccore.model.ServiceMetaInfo;
import com.shaoxia.rpccore.model.ServiceRegisterInfo;
import com.shaoxia.rpccore.registry.LocalRegistry;
import com.shaoxia.rpccore.registry.Registry;
import com.shaoxia.rpccore.registry.RegistryFactory;
import com.shaoxia.rpccore.server.HttpServer;
import com.shaoxia.rpccore.server.VertxHttpServer;
import com.shaoxia.rpccore.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wjc28
 * @version 1.0
 * @description: 示例提供
 * @date 2024-06-23 19:11
 */
public class ProviderExample {
	public static void main(String[] args) {
		List<ServiceRegisterInfo> serviceRegisterInfos = new ArrayList<>();
		ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
		serviceRegisterInfos.add(serviceRegisterInfo);
		ProviderBootstrap.init(serviceRegisterInfos);
	}
}
