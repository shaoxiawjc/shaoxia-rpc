package com.shaoxia.shaoxiarpcbootstarter.annotation;

import com.shaoxia.rpccore.constant.RpcConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务提供者
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface SXService {
	/**
	 * 接口类型
	 * @return
	 */
	Class<?> interfaceClass() default void.class;


	/**
	 * 版本
	 */
	String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;


}
