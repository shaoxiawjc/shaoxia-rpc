package com.shaoxia.rpccore.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.model.RpcRequest;
import com.shaoxia.rpccore.model.RpcResponse;
import com.shaoxia.rpccore.model.ServiceMetaInfo;
import com.shaoxia.rpccore.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author wjc28
 * @version 1.0
 * @description: 客户端实例
 * @date 2024-07-02 11:02
 */
@Slf4j
public class VertxTcpClient {
	public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
		System.out.println("发送了TCP请求");
		// 发送 TCP 请求
		Vertx vertx = Vertx.vertx();
		NetClient netClient = vertx.createNetClient();
		CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
		netClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(),
				result -> {
					if (!result.succeeded()) {
						System.err.println("Failed to connect to TCP server");
						return;
					}
					NetSocket socket = result.result();
					// 发送数据
					// 构造消息
					ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
					ProtocolMessage.Header header = new ProtocolMessage.Header();
					header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
					header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
					header.setSerializer((byte) ProtocolMessageSerializerEnum.getByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
					header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
					// 生成全局请求 ID
					header.setRequestId(IdUtil.getSnowflakeNextId());
					protocolMessage.setHeader(header);
					protocolMessage.setBody(rpcRequest);

					// 编码请求
					try {
						Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
						socket.write(encodeBuffer);
					} catch (IOException e) {
						throw new RuntimeException("协议消息编码错误");
					}

					// 接收响应
					TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(
							buffer -> {
								try {
									ProtocolMessage<RpcResponse> rpcResponseProtocolMessage =
											(ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
									responseFuture.complete(rpcResponseProtocolMessage.getBody());
								} catch (IOException e) {
									throw new RuntimeException("协议消息解码错误");
								}
							}
					);
					socket.handler(bufferHandlerWrapper);
				});

		RpcResponse rpcResponse = responseFuture.get();
		// 记得关闭连接
		netClient.close();
		return rpcResponse;
	}

}
