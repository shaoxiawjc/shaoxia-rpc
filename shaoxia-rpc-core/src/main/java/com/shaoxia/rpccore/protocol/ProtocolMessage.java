package com.shaoxia.rpccore.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wjc28
 * @version 1.0
 * @description: 协议消息结构
 * @date 2024-07-02 9:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
	/**
	 * 消息头
	 */
	private Header header;

	/**
	 * 请求体
	 */
	private T body;

	@Data
	public static class Header{
		/**
		 * 魔数，保证安全性
		 */
		private byte magic;

		/**
		 * 版本号
		 */
		private byte version;

		/**
		 * 序列化器
		 */
		private byte serializer;

		/**
		 * 类型 请求or响应
		 */
		private byte type;

		/**
		 * 状态
		 */
		private byte status;

		/**
		 * 请求Id
		 */
		private long requestId;

		/**
		 * 请求体长度
		 */
		private int bodyLength;
	}
}
