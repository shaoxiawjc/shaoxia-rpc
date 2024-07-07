package com.shaoxia.rpccore.config;

import com.shaoxia.rpccore.loadbalancer.LoadBalancer;
import com.shaoxia.rpccore.loadbalancer.LoadBalancerKeys;
import com.shaoxia.rpccore.serializer.SerializerKeys;
import lombok.Data;

/**
 * @author wjc28
 * @version 1.0
 * @description: RPC框架配置
 * @date 2024-06-25 18:08
 */
@Data
public class RpcConfig {
	/**
	 * 名称
	 */
	private String name = "shaoxia-rpc";

	/**
	 * 版本号
	 */
	private String version = "1.0";

	/**
	 * 服务器主机名
	 */
	private String serverHost = "localhost";

	/**
	 * 服务端口号
	 */
	private Integer serverPort = 8080;

	/**
	 * 是否开启mock
	 */
	private boolean mock = false;

	/**
	 * 序列化器
	 */
	private String serializer = SerializerKeys.JDK;

	/**
	 * 注册中心配置
	 */
	private RegistryConfig registryConfig = new RegistryConfig();

	/**
	 * 负载均衡策略
	 */
	private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;
}
