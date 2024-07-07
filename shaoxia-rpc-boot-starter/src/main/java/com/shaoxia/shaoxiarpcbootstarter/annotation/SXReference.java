package com.shaoxia.shaoxiarpcbootstarter.annotation;

import com.shaoxia.rpccore.constant.RpcConstant;
import com.shaoxia.rpccore.fault.retry.RetryStrategyKeys;
import com.shaoxia.rpccore.fault.tolerant.TolerantStrategyKeys;
import com.shaoxia.rpccore.loadbalancer.LoadBalancerKeys;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wjc28
 * @version 1.0
 * @description: 消费者引用注解
 * @date 2024-07-07 17:12
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SXReference {
	/**
	 * 接口类型
	 * @return
	 */
	Class<?> interfaceClas() default void.class;

	/**
	 * 版本
	 * @return
	 */
	String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;


	/**
	 * 负载均衡器
	 * @return
	 */
	String loadBalancer() default LoadBalancerKeys.ROUND_ROBIN;


	/**
	 * 重试机制
	 * @return
	 */
	String retryStrategy() default RetryStrategyKeys.FIXED_INTERVAL;


	/**
	 * 容错机制
	 * @return
	 */
	String tolerantStrategy() default TolerantStrategyKeys.FAIL_SAFE;


	/**
	 * mock
	 * @return
	 */
	boolean mock() default false;
}
