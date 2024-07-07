package com.shaoxia.rpccore.fault.tolerant;

import com.shaoxia.rpccore.model.RpcResponse;

import java.util.Map;

/**
 * @author wjc28
 * @version 1.0
 * @description: 快速处理
 * @date 2024-07-07 15:46
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
	@Override
	public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
		throw new RuntimeException("服务报错", e);

	}
}
