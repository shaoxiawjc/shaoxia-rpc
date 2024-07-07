package com.shaoxia.rpccore.fault.retry;

import com.github.rholder.retry.*;
import com.shaoxia.rpccore.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author wjc28
 * @version 1.0
 * @description: 固定时间间隔重试
 * @date 2024-07-07 11:39
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy{
	@Override
	public RpcResponse doRetry(Callable<RpcResponse> rpcResponseCallable) throws Exception {
		Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
				.retryIfExceptionOfType(Exception.class)
				.withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
				.withStopStrategy(StopStrategies.stopAfterAttempt(3))
				.withRetryListener(new RetryListener() {
					@Override
					public <V> void onRetry(Attempt<V> attempt) {
						log.info("重试次数 {}", attempt.getAttemptNumber());
					}
				}).build();
		return retryer.call(rpcResponseCallable);
	}
}
