package com.shaoxia.shaoxiarpcbootstarter.bootstrap;

import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.config.RpcConfig;
import com.shaoxia.rpccore.server.tcp.VertxTcpServer;
import com.shaoxia.shaoxiarpcbootstarter.annotation.EnableSX;
import com.shaoxia.shaoxiarpcbootstarter.annotation.SXReference;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wjc28
 * @version 1.0
 * @description: RPC全局启动类
 * @date 2024-07-07 19:04
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableSX.class.getName())
				.get("needServer");

		// RPC框架初始化
		RpcApplication.init();

		// 全局配置
		final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

		// 启动服务器
		if (needServer){
			VertxTcpServer vertxTcpServer = new VertxTcpServer();
			vertxTcpServer.doStart(rpcConfig.getServerPort());
		}else {
			log.info("不启动服务");
		}
	}
}
