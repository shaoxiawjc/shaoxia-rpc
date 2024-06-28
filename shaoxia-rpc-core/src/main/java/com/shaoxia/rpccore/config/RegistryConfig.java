package com.shaoxia.rpccore.config;

import lombok.Data;

/**
 * @author wjc28
 * @version 1.0
 * @description: 注册中心配置
 * @date 2024-06-28 11:48
 */
@Data
public class RegistryConfig {
	/**
	 * 注册中心类别
	 */
	private String registry = "etcd";

	/**
	 * 注册中心地址
	 */
	private String registryAddress = "http://localhost:2379";

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 超时时间
	 */
	private Long timeout = 10000L;


}
