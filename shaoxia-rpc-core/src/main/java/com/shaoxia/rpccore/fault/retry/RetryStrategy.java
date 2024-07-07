package com.shaoxia.rpccore.fault.retry;

import com.shaoxia.rpccore.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 */
public interface RetryStrategy {

	/**
	 * 重试
	 * @param rpcResponseCallable 任务
	 * @return
	 * @throws Exception
	 */
	RpcResponse doRetry(Callable<RpcResponse> rpcResponseCallable) throws Exception;
}
