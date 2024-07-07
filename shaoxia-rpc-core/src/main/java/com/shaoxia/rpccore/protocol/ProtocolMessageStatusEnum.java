package com.shaoxia.rpccore.protocol;

import lombok.Getter;

/**
 * @author wjc28
 * @version 1.0
 * @description: 协议状态枚举类
 * @date 2024-07-02 10:25
 */
@Getter
public enum ProtocolMessageStatusEnum {
	OK("ok",20),
	BAD_REQUEST("badRequest",40),
	BAD_RESPONSE("badResponse",50)
	;

	private final String text;

	private final int value;

	ProtocolMessageStatusEnum(String text, int value) {
		this.text = text;
		this.value = value;
	}

	public static ProtocolMessageStatusEnum getByValue(int value){
		for (ProtocolMessageStatusEnum anEnum : ProtocolMessageStatusEnum.values()) {
			if (anEnum.getValue() == value){
				return anEnum;
			}
		}
		return null;
	}
}
