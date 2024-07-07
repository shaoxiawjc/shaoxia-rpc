package com.shaoxia.rpccore.fault.tolerant;

import com.shaoxia.rpccore.spi.SpiLoader;

/**
 * @author wjc28
 * @version 1.0
 * @description: 容错机制工厂类
 * @date 2024-07-07 16:13
 */
public class TolerantStrategyFactory {
	static {
		SpiLoader.load(TolerantStrategy.class);
	}

	public static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY = new FailSafeTolerantStrategy();

	public static TolerantStrategy getInstance(String key){
		return SpiLoader.getInstance(TolerantStrategy.class,key);
	}
}
