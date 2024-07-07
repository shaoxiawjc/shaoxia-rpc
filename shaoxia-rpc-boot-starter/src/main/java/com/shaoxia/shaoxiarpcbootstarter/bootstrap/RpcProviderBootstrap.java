package com.shaoxia.shaoxiarpcbootstarter.bootstrap;

import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.config.RpcConfig;
import com.shaoxia.rpccore.model.ServiceMetaInfo;
import com.shaoxia.rpccore.registry.LocalRegistry;
import com.shaoxia.rpccore.registry.Registry;
import com.shaoxia.rpccore.registry.RegistryFactory;
import com.shaoxia.shaoxiarpcbootstarter.annotation.SXService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author wjc28
 * @version 1.0
 * @description: Rpc 服务提供者启动
 * @date 2024-07-07 19:19
 */
@Slf4j
public class RpcProviderBootstrap implements BeanPostProcessor {
	/**
	 * 在Bean初始化后注册
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> beanClass = bean.getClass();
		SXService sxService = beanClass.getAnnotation(SXService.class);
		if (sxService != null){
			// 需要注册信息
			Class<?> interfaceClass = sxService.interfaceClass();
			// 默认值处理
			if (interfaceClass == void.class){
				interfaceClass = beanClass.getInterfaces()[0];
			}
			String serviceName = interfaceClass.getName();
			String serviceVersion = sxService.serviceVersion();

			// 注册服务
			LocalRegistry.register(serviceName,beanClass);
			RpcConfig rpcConfig = RpcApplication.getRpcConfig();
			Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
			ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
			serviceMetaInfo.setServiceName(serviceName);
			serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
			serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
			serviceMetaInfo.setServiceVersion(serviceVersion);
			try {
				registry.register(serviceMetaInfo);
			} catch (Exception e) {
				throw new RuntimeException("服务注册异常" +  serviceName , e);
			}
		}
		return BeanPostProcessor.super.postProcessAfterInitialization(bean,beanName);
	}
}
