package com.shaoxia.rpccore.protocol;

import cn.hutool.core.util.IdUtil;
import com.shaoxia.rpccore.constant.RpcConstant;
import com.shaoxia.rpccore.model.RpcRequest;
import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-07-05 15:00
 */
public class ProtocolMessageTest {
	@Test
	public void testEncodeAndDecode() throws IOException {
		ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
		ProtocolMessage.Header header = new ProtocolMessage.Header();
		header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
		header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
		header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
		header.setType((byte)ProtocolMessageTypeEnum.REQUEST.getKey());
		header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
		header.setRequestId(IdUtil.getSnowflakeNextId());
		header.setBodyLength(0);
		RpcRequest rpcRequest = new RpcRequest("myService",
				"myMethod",
				RpcConstant.DEFAULT_SERVICE_VERSION,
				new Class[]{String.class},
				new Object[]{"aaa", "bbb"});
		protocolMessage.setHeader(header);
		protocolMessage.setBody(rpcRequest);

		Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
		ProtocolMessage<?> decode = ProtocolMessageDecoder.decode(encode);
		Assert.assertNotNull(decode);

	}
}
