package com.shaoxia.rpccore.loadbalancer;

import com.shaoxia.rpccore.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author wjc28
 * @version 1.0
 * @description: 一致性哈希算法
 * @date 2024-07-07 10:49
 */
public class ConsistHashLoadBalancer implements LoadBalancer{
	/**
	 * 虚拟节点
	 */
	private final TreeMap<Integer,ServiceMetaInfo> virtualNode = new TreeMap<>();

	/**
	 * 虚拟节点数
	 */
	private static final int VIRTUAL_NODE_NUM = 100;

	@Override
	public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
		if (serviceMetaInfoList.isEmpty()){
			return null;
		}
		for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
			for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
				int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
				virtualNode.put(hash,serviceMetaInfo);
			}
		}

		int hash = getHash(requestParams);
		Map.Entry<Integer, ServiceMetaInfo> ceilingEntry = virtualNode.ceilingEntry(hash);
		if (ceilingEntry == null){
			ceilingEntry =  virtualNode.firstEntry();
		}
		return ceilingEntry.getValue();
	}

	private int getHash(Object key) {
		return key.hashCode();
	}
}
