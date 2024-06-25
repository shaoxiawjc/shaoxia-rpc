package com.shaoxia.rpccore.config;

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
}
