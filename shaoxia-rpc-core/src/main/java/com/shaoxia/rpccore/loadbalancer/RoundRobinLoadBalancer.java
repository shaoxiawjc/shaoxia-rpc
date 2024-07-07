package com.shaoxia.rpccore.loadbalancer;

import com.shaoxia.rpccore.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wjc28
 * @version 1.0
 * @description: 轮询负载均衡
 * @date 2024-07-07 10:44
 */
public class RoundRobinLoadBalancer implements LoadBalancer{
	/**
	 * 当前计数的下标
	 */
	private final AtomicInteger currentIndex = new AtomicInteger(0);


	@Override
	public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
		if (serviceMetaInfoList.isEmpty()){
			return null;
		}
		int size = serviceMetaInfoList.size();
		if (size == 1) {
			return serviceMetaInfoList.get(0);
		}
		return serviceMetaInfoList.get(currentIndex.getAndIncrement() % size);
	}
}
