package com.shaoxia.rpccore.fault.tolerant;

import com.shaoxia.rpccore.model.RpcResponse;

import java.util.Map;

/**
 * @author wjc28
 * @version 1.0
 * @description: 容错策略
 * @date 2024-07-07 15:43
 */
public interface TolerantStrategy {

	RpcResponse doTolerant(Map<String, Object> context ,Exception e);
}
