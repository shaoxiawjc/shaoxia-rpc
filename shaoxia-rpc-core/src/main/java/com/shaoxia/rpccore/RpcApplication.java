package com.shaoxia.rpccore;

import com.shaoxia.rpccore.config.RpcConfig;
import com.shaoxia.rpccore.constant.RpcConstant;
import com.shaoxia.rpccore.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wjc28
 * @version 1.0
 * @description: 全局配置（单例模式）
 * @date 2024-06-25 22:57
 */
@Slf4j
public class RpcApplication {
	private static volatile RpcConfig rpcConfig;

	/**
	 * 框架初始化，支持传入自定义配置
	 * @param newRpcConfig
	 */
	public static void init(RpcConfig newRpcConfig){
		rpcConfig = newRpcConfig;
		log.info("rpc inti,config = {}",newRpcConfig.toString());
	}


	/**
	 * 初始化
	 */
	public static void init(){
		RpcConfig newRpcConfig;
		try {
			newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
		}catch (Exception e){
			// 配置加载失败，使用默认值
			newRpcConfig = new RpcConfig();
		}
		init(newRpcConfig);
	}

	public static RpcConfig getRpcConfig(){
		// 双重检查锁
		// 第一次检查
		if (rpcConfig == null) {
			// 同步当前类，确保只有一个线程操作
			synchronized (RpcApplication.class) {
				// 第二次检查
				if (rpcConfig == null){
					init();
				}
			}
		}
		return rpcConfig;
	}
}
