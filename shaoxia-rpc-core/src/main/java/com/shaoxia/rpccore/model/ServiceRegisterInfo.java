package com.shaoxia.rpccore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-07-07 16:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo<T> {
	/**
	 * 注册的服务名
	 */
	private String serviceName;

	/**
	 * 实现类的class对象
	 */
	private Class<? extends T> impClass;
}
