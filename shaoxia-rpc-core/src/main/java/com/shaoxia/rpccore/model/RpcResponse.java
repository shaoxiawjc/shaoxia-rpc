package com.shaoxia.rpccore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wjc28
 * @version 1.0
 * @description: RPC响应类
 * @date 2024-06-24 22:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcResponse implements Serializable {
	/**
	 * 响应数据
	 */
	private Object data;

	/**
	 * 响应数据类型
	 */
	private Class<?> dataType;

	/**
	 * 响应信息
	 */
	private String message;

	/**
	 * 异常信息
	 */
	private Exception exception;

}
