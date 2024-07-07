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
		Vertx vertx = Vertx.vertx();
		NetClient netClient = vertx.createNetClient();
		CompletableFuture<RpcResponse> rpcResponseCompletableFuture = new CompletableFuture<>();
		netClient.connect(serviceMetaInfo.getServicePort(),serviceMetaInfo.getServiceHost(),
				result->{
					if (!result.succeeded()){
						log.error("Connect to Server Failed");
						return;
					}
					NetSocket socket = result.result();
					// 构造并发送消息
					ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
					protocolMessage.setBody(rpcRequest);
					ProtocolMessage.Header header = new ProtocolMessage.Header();
					header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
					header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
					header.setSerializer((byte) ProtocolMessageSerializerEnum.getByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
					header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
					header.setRequestId(IdUtil.getSnowflakeNextId());
					protocolMessage.setHeader(header);

					// 编码请求
					try {
						Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
						socket.write(encode);
					} catch (IOException e) {
						throw new RuntimeException("协议消息编码错误");
					}

					// 接收响应
					TcpBufferHandlerWrapper handlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
						try {
							ProtocolMessage<RpcResponse> decode = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
							rpcResponseCompletableFuture.complete(decode.getBody());
						} catch (IOException e) {
							throw new RuntimeException("协议消息解码失败");
						}
					});
					socket.handler(handlerWrapper);
				});
		RpcResponse rpcResponse = rpcResponseCompletableFuture.get();
		netClient.close();
		return rpcResponse;
	}

}
