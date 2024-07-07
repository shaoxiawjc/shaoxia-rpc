package com.shaoxia.rpccore.loadbalancer;

import com.shaoxia.rpccore.spi.SpiLoader;

/**
 * @author wjc28
 * @version 1.0
 * @description: 负载均衡工厂模式
 * @date 2024-07-07 11:00
 */
public class LoadBalancerFactory {
	static {
		SpiLoader.load(LoadBalancer.class);
	}

	/**
	 * 默认负载均衡器
	 */
	private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

	/**
	 * 获取实例
	 * @param key
	 * @return
	 */
	public static LoadBalancer getInstance(String key){
		return SpiLoader.getInstance(LoadBalancer.class,key);
	}
}
