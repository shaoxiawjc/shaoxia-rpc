package com.shaoxia.rpccore.fault.retry;

import com.shaoxia.rpccore.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @author wjc28
 * @version 1.0
 * @description: 不重试策略
 * @date 2024-07-07 11:36
 */
public class NoRetryStrategy implements RetryStrategy{
	/**
	 * 直接进行一次任务
	 * @param rpcResponseCallable 任务
	 * @return
	 * @throws Exception
	 */
	@Override
	public RpcResponse doRetry(Callable<RpcResponse> rpcResponseCallable) throws Exception {
		return rpcResponseCallable.call();
	}
}
