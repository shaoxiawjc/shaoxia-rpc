package com.shaoxia.shaoxiarpcbootstarter.bootstrap;

import com.shaoxia.rpccore.proxy.ServiceProxyFactory;
import com.shaoxia.shaoxiarpcbootstarter.annotation.SXReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @author wjc28
 * @version 1.0
 * @description: 消费者启动类
 * @date 2024-07-07 19:29
 */
@Slf4j
public class RpcConsumerBootstrap implements BeanPostProcessor {
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> beanClass = bean.getClass();
		Field[] declaredFields = beanClass.getDeclaredFields();
		for (Field field : declaredFields) {
			SXReference sxReference = field.getAnnotation(SXReference.class);
			if (sxReference != null){
				// 为属性生成代理对象
				log.info("---生成代理对象中----");
				Class<?> interfaceClas = sxReference.interfaceClas();
				if (interfaceClas == void.class){
					interfaceClas = field.getType();
				}
				field.setAccessible(true);
				Object proxy = ServiceProxyFactory.getProxy(interfaceClas);
				try {
					field.set(bean,proxy);
					field.setAccessible(false);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("为字段注入代理对象失败",e);
				}
			}
		}
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}
}
