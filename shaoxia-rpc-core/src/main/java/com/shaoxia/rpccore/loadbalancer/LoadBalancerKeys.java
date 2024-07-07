package com.shaoxia.rpccore.loadbalancer;

/**
 * 负载均衡键
 */
public interface LoadBalancerKeys {
	String ROUND_ROBIN = "roundRobin";

	String RANDOM = "random";

	String CONSIST_HASH = "consistHash";
}
