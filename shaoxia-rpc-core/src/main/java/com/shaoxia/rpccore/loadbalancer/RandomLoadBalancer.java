package com.shaoxia.rpccore.loadbalancer;

import com.shaoxia.rpccore.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author wjc28
 * @version 1.0
 * @description: 随机负载均衡
 * @date 2024-07-07 10:48
 */
public class RandomLoadBalancer implements LoadBalancer{
	private final Random random = new Random();

	@Override
	public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
		if (serviceMetaInfoList.isEmpty()){
			return null;
		}
		int size = serviceMetaInfoList.size();
		if (size == 1) {
			return serviceMetaInfoList.get(0);
		}
		return serviceMetaInfoList.get(random.nextInt(size));
	}
}
