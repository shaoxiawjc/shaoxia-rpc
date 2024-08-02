package com.shaoxia.rpccore.server.tcp;

import com.shaoxia.rpccore.model.RpcRequest;
import com.shaoxia.rpccore.model.RpcResponse;
import com.shaoxia.rpccore.protocol.*;
import com.shaoxia.rpccore.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author wjc28
 * @version 1.0
 * @description: Tcp请求处理器
 * @date 2024-07-06 21:49
 */
@Slf4j
public class TcpServerHandler implements Handler<NetSocket> {
	@Override
	public void handle(NetSocket socket) {
		log.info("handler socket");
		TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
			// 接受请求，解码
			log.info("接收请求，解码");
			ProtocolMessage<RpcRequest> protocolMessage;
			try {
				protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
				log.info("protocolMessage{}", protocolMessage);
			} catch (IOException e) {
				throw new RuntimeException("协议消息解码错误");
			}
			RpcRequest rpcRequest = protocolMessage.getBody();
			ProtocolMessage.Header header = protocolMessage.getHeader();

			log.info("rpcRequest{}", rpcRequest);
			// 处理请求
			// 构造响应结果对象
			RpcResponse rpcResponse = new RpcResponse();
			try {
				// 获取要调用的服务实现类，通过反射调用
				Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
				Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
				Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
				// 封装返回结果
				rpcResponse.setData(result);
				rpcResponse.setDataType(method.getReturnType());
				rpcResponse.setMessage("ok");
			} catch (Exception e) {
				e.printStackTrace();
				rpcResponse.setMessage(e.getMessage());
				rpcResponse.setException(e);
			}

			// 发送响应，编码
			header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
			header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
			ProtocolMessage<RpcResponse> responseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
			log.info("responseProtocolMessage{}", responseProtocolMessage);
			try {
				Buffer encode = ProtocolMessageEncoder.encode(responseProtocolMessage);
				socket.write(encode);
			} catch (IOException e) {
				throw new RuntimeException("协议消息编码错误");
			}
		});

		socket.handler(bufferHandlerWrapper);
	}
}
