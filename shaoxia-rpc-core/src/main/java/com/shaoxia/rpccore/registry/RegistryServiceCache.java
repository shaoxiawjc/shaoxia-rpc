package com.shaoxia.rpccore.registry;

import com.shaoxia.rpccore.model.ServiceMetaInfo;

import java.util.List;

/**
 * @author wjc28
 * @version 1.0
 * @description: 存储注册的服务的缓存
 * @date 2024-06-29 22:33
 */
public class RegistryServiceCache {
	/**
	 * 服务缓存
	 */
	List<ServiceMetaInfo> serviceMetaInfosCache;

	/**
	 * 更新缓存
	 * @param newServiceMetaInfos
	 */
	void writeCache(List<ServiceMetaInfo> newServiceMetaInfos){
		this.serviceMetaInfosCache = newServiceMetaInfos;
	}

	/**
	 * 读缓存
	 * @return
	 */
	List<ServiceMetaInfo> readCache(){
		return this.serviceMetaInfosCache;
	}

	/**
	 * 清空缓存
	 */
	void clear(){
		this.serviceMetaInfosCache.clear();
	}


}
