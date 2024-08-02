package com.shaoxia.shaoxiarpcbootstarter.annotation;

import com.shaoxia.shaoxiarpcbootstarter.bootstrap.RpcConsumerBootstrap;
import com.shaoxia.shaoxiarpcbootstarter.bootstrap.RpcInitBootstrap;
import com.shaoxia.shaoxiarpcbootstarter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC注解驱动
 * @author wjc28
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableSX {
	/**
	 * 是否需要开启server
	 * @return
	 */
	boolean needServer() default true;
}
