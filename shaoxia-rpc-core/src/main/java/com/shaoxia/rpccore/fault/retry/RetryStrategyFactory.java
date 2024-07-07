package com.shaoxia.rpccore.fault.retry;

import com.shaoxia.rpccore.spi.SpiLoader;

/**
 * @author wjc28
 * @version 1.0
 * @description: 重试机制工厂类
 * @date 2024-07-07 11:50
 */
public class RetryStrategyFactory {
	static {
		SpiLoader.load(RetryStrategy.class);
	}

	public static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

	public static RetryStrategy getInstance(String keys){
		return SpiLoader.getInstance(RetryStrategy.class,keys);
	}
}
