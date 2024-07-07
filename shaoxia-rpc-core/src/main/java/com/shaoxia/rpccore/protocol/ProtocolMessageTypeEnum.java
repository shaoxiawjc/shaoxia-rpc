package com.shaoxia.rpccore.protocol;

import lombok.Getter;

@Getter
public enum ProtocolMessageTypeEnum {
	REQUEST(0),
	RESPONSE(1),
	HEART_BEAT(2),
	OTHER(3)
	;

	private final int key;

	ProtocolMessageTypeEnum(int key) {
		this.key = key;
	}

	public static ProtocolMessageTypeEnum getByKey(int key){
		for (ProtocolMessageTypeEnum anEnum : ProtocolMessageTypeEnum.values()) {
			if (anEnum.getKey() == key){
				return anEnum;
			}
		}
		return null;
	}
}
