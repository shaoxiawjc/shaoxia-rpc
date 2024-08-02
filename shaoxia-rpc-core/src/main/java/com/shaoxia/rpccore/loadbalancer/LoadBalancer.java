package com.shaoxia.rpccore.loadbalancer;

import com.shaoxia.rpccore.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * @author wjc28
 */
public interface LoadBalancer {
	/**
	 * 选择服务器调用
	 * @param requestParams 请求参数
	 * @param serviceMetaInfoList  待选择的服务列表
	 * @return
	 */
	ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
