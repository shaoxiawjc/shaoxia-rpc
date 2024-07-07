package com.shaoxia.rpccore.fault.tolerant;

import com.shaoxia.rpccore.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author wjc28
 * @version 1.0
 * @description: 静默处理
 * @date 2024-07-07 15:47
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
	@Override
	public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
		log.info("静默处理", e);
		return new RpcResponse();
	}
}
