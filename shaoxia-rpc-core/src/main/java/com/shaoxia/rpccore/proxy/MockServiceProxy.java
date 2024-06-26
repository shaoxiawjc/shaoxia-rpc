package com.shaoxia.rpccore.proxy;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author wjc28
 * @version 1.0
 * @description: mock 服务 jdk动态代理
 * @date 2024-06-26 13:50
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
	private static Faker faker = new Faker(Locale.CHINA);
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 根据返回类型生成默认值对象
		Class<?> returnType = method.getReturnType();
		log.info("mock invoke {}",method.getName());

		return getDefaultObject(returnType);
	}

	private Object getDefaultObject(Class<?> type){
		if (type == String.class) {
			return faker.lorem().sentence();
		} else if (type == int.class || type == Integer.class) {
			return faker.number().numberBetween(0, 100);
		} else if (type == long.class || type == Long.class) {
			return faker.number().numberBetween(0L, 10000L);
		} else if (type == double.class || type == Double.class) {
			return faker.number().randomDouble(2, 0, 100);
		} else if (type == boolean.class || type == Boolean.class) {
			return faker.bool().bool();
		} else if (type == Date.class) {
			return faker.date().past(365, TimeUnit.DAYS);
		} else {
			// For complex types, create an instance and fill fields recursively
			try {
				Object instance = type.getDeclaredConstructor().newInstance();
				for (Field field : type.getDeclaredFields()) {
					field.setAccessible(true);
					field.set(instance, getDefaultObject(field.getType()));
				}
				return instance;
			} catch (Exception e) {
				throw new RuntimeException("Failed to generate mock data for type: " + type, e);
			}
		}
	}
}
