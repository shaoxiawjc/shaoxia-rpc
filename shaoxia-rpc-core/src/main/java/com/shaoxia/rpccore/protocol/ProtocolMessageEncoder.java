package com.shaoxia.rpccore.protocol;

import com.shaoxia.rpccore.serializer.Serializer;
import com.shaoxia.rpccore.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @author wjc28
 * @version 1.0
 * @description: 编码器
 * @date 2024-07-02 11:16
 */
public class ProtocolMessageEncoder {

	public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
		Buffer buffer = Buffer.buffer();
		if (protocolMessage == null || protocolMessage.getHeader() == null) {
			return buffer;
		}

		ProtocolMessage.Header header = protocolMessage.getHeader();
		// 依次向缓冲区写入字节
		buffer.appendByte(header.getMagic());
		buffer.appendByte(header.getVersion());
		byte serializerType = header.getSerializer();
		buffer.appendByte(serializerType);
		buffer.appendByte(header.getType());
		buffer.appendByte(header.getStatus());
		buffer.appendLong(header.getRequestId());
		ProtocolMessageSerializerEnum messageSerializerEnum = ProtocolMessageSerializerEnum.getByKey(serializerType);
		if (messageSerializerEnum == null){
			throw new RuntimeException(" 协议不存在");
		}
		Serializer serializer = SerializerFactory.getInstance(messageSerializerEnum.getValue());
		byte[] bodyBytes = serializer.serialize(protocolMessage.getBody());
		// 写入 body 长度和数据
		buffer.appendInt(bodyBytes.length);
		buffer.appendBytes(bodyBytes);
		return buffer;

	}
}
