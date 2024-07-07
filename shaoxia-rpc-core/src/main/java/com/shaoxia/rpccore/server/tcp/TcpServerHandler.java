package com.shaoxia.rpccore.server.tcp;

import com.shaoxia.rpccore.model.RpcRequest;
import com.shaoxia.rpccore.model.RpcResponse;
import com.shaoxia.rpccore.protocol.ProtocolMessage;
import com.shaoxia.rpccore.protocol.ProtocolMessageDecoder;
import com.shaoxia.rpccore.protocol.ProtocolMessageEncoder;
import com.shaoxia.rpccore.protocol.ProtocolMessageTypeEnum;
import com.shaoxia.rpccore.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author wjc28
 * @version 1.0
 * @description: Tcp请求处理器
 * @date 2024-07-06 21:49
 */
public class TcpServerHandler implements Handler<NetSocket> {
	@Override
	public void handle(NetSocket netSocket) {
		new TcpBufferHandlerWrapper(buffer -> {
			ProtocolMessage<RpcRequest> protocolMessage;
			try {
				protocolMessage = (ProtocolMessage<RpcRequest>)ProtocolMessageDecoder.decode(buffer);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			RpcRequest rpcRequest = protocolMessage.getBody();
			// 处理请求
			RpcResponse rpcResponse = new RpcResponse();
			try {
				Class<?> impClass = LocalRegistry.get(rpcRequest.getServiceName());
				Method method = impClass.getMethod(rpcRequest.getMethodName());
				Object result = method.invoke(impClass.newInstance(), rpcRequest.getArgs());
				rpcResponse.setData(result);
				rpcResponse.setDataType(method.getReturnType());
				rpcResponse.setMessage("ok");
			} catch (Exception e) {
				e.printStackTrace();
				rpcResponse.setMessage(e.getMessage());
				rpcResponse.setException(e);
			}
			// 发送响应
			ProtocolMessage.Header header = protocolMessage.getHeader();
			header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
			ProtocolMessage<RpcResponse> message = new ProtocolMessage<>(header, rpcResponse);
			try {
				Buffer encode = ProtocolMessageEncoder.encode(message);
				netSocket.write(encode);
			} catch (IOException e) {
				throw new RuntimeException("协议消息编码错误");
			}
		});
	}
}
