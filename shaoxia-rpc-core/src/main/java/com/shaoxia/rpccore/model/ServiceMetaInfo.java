package com.shaoxia.rpccore.model;

import cn.hutool.core.util.StrUtil;
import com.shaoxia.rpccore.constant.RpcConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wjc28
 * @version 1.0
 * @description: 服务注册基本信息
 * @date 2024-06-28 11:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceMetaInfo {
	/**
	 * 服务名称
	 */
	private String serviceName;

	/**
	 * 服务版本
	 */
	private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;
	/**
	 * 服务地址
	 */
	private String serviceHost;

	/**
	 * 服务端口
	 */
	private Integer servicePort;

	/**
	 * 服务分组
	 */
	private String serviceGroup = "default";

	/**
	 * 获取服务键名
	 * @return
	 */
	public String getServiceKey() {
		return String.format("%s:%s",serviceName,serviceVersion);
	}

	/**
	 * 获取服务注册节点键名
	 * @return
	 */
	public String getServiceNodeKey(){
		return String.format("%s/%s:%s",getServiceKey(),serviceHost,servicePort);
	}

	/**
	 * 获取完整的服务地址
	 * @return
	 */
	public String getServiceAddress(){
		if (!StrUtil.contains(serviceHost,"http")){
			return String.format("http://%s:%s",serviceHost,servicePort);
		}
		return String.format("%s:%s",serviceHost,servicePort);
	}



}
