package com.shaoxia.rpccore.protocol;

import com.shaoxia.rpccore.model.RpcRequest;
import com.shaoxia.rpccore.model.RpcResponse;
import com.shaoxia.rpccore.serializer.Serializer;
import com.shaoxia.rpccore.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;
import java.util.Objects;

/**
 * @author wjc28
 * @version 1.0
 * @description: 协议消息解码器
 * @date 2024-07-05 14:18
 */
public class ProtocolMessageDecoder {
	public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
		// 从指定位置读出 buffer
		byte magic = buffer.getByte(0);
		if (magic != ProtocolConstant.PROTOCOL_MAGIC){
			System.out.println(magic);
			throw new RuntimeException("魔数不合法");
		}
		ProtocolMessage.Header header = new ProtocolMessage.Header();
		header.setMagic(magic);
		header.setVersion(buffer.getByte(1));
		header.setSerializer(buffer.getByte(2));
		header.setType(buffer.getByte(3));
		header.setStatus(buffer.getByte(4));
		header.setRequestId(buffer.getLong(5));
		header.setBodyLength(buffer.getInt(13));
		// 解决粘包问题，只读取指定长度的数据
		byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());
		// 解析消息体
		ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getByKey(header.getSerializer());
		if (Objects.isNull(serializerEnum)){
			throw new RuntimeException("序列化器协议不存在");
		}
		Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
		ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getByKey(header.getType());
		if (messageTypeEnum == null){
			throw new RuntimeException("序列化的消息类型不存在");
		}
		switch (messageTypeEnum){
			case REQUEST -> {
				RpcRequest request = serializer.deserialize(bodyBytes, RpcRequest.class);
				return new ProtocolMessage<>(header,request);
			}
			case RESPONSE -> {
				RpcResponse response = serializer.deserialize(bodyBytes, RpcResponse.class);
				return new ProtocolMessage<>(header,response);
			}
			case HEART_BEAT, OTHER -> {
				return null;
			}
			default -> {
				throw new RuntimeException("暂不支持该消息类型");
			}
		}


	}
}
