package com.shaoxia.rpccore.protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wjc28
 */

@Getter
public enum ProtocolMessageSerializerEnum {
	JDK(0,"jdk"),
	JSON(1,"json"),
	KRYO(2,"kryo"),
	HESSIAN(3,"hessian")
	;

	private final int key;

	private final String value;

	ProtocolMessageSerializerEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public static List<String> geyValues(){
		return Arrays.stream(values()).map(item -> item.value)
				.collect(Collectors.toList());
	}

	public static ProtocolMessageSerializerEnum getByKey(int key){
		for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
			if (anEnum.getKey() == key){
				return anEnum;
			}
		}
		return null;
	}

	public static ProtocolMessageSerializerEnum getByValue(String value){
		if (ObjectUtil.isEmpty(value)) {
			return null;
		}
		for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
			if (anEnum.getValue().equals(value)){
				return anEnum;
			}
		}
		return null;
	}
}
