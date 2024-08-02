package com.shaoxia.rpccore.registry;

import com.shaoxia.rpccore.config.RegistryConfig;
import com.shaoxia.rpccore.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 注册中心
 * @author wjc28
 */
public interface Registry {
	/**
	 * 初始化
	 * @param registryConfig
	 */
	void init(RegistryConfig registryConfig);

	/**
	 * 服务注册
	 * @param serviceMetaInfo
	 */
	void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

	/**
	 * 服务注销
	 * @param serviceMetaInfo
	 */
	void unRegister(ServiceMetaInfo serviceMetaInfo);

	/**
	 * 服务发现
	 * @param serviceKey
	 * @return
	 */
	List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

	/**
	 * 服务销毁
	 */
	void destroy();

	/**
	 * 心跳机制（提供者）
	 */
	void heartBeat();

	/**
	 * 监听一个服务（消费者端）
	 * @param watchNodeKey
	 */
	void watch(String watchNodeKey);
}
