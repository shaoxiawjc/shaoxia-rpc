package com.shaoxia.rpceasy.server;

import com.shaoxia.rpceasy.model.RpcRequest;
import com.shaoxia.rpceasy.model.RpcResponse;
import com.shaoxia.rpceasy.registry.LocalRegistry;
import com.shaoxia.rpceasy.serializer.JdkSerializer;
import com.shaoxia.rpceasy.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author wjc28
 * @version 1.0
 * @description: 处理Http请求
 * @date 2024-06-24 22:34
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
	@Override
	public void handle(HttpServerRequest request) {
		// 指定序列化器
		final Serializer serializer = new JdkSerializer();

		// 记录日志
		System.out.println("Receive request: " + request.method() + " " + request.uri());

		// 异步处理请求
		request.bodyHandler(body -> {
			byte[] bytes = body.getBytes();
			RpcRequest rpcRequest = null;

			try {
				// 反序列化
				rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println(rpcRequest);

			// 构造响应对象
			RpcResponse rpcResponse = new RpcResponse();
			// 如果请求对象为null，直接返回
			if (Objects.isNull(rpcRequest)){
				rpcResponse.setMessage("rpcRequest is null");
				doResponse(request,rpcResponse,serializer);
				return;
			}

			try {
				// 获取调用的服务实现类，通过反射调用
				Class<?> impClass = LocalRegistry.get(rpcRequest.getServiceName());
				Method method = impClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
				Object result = method.invoke(impClass.newInstance(), rpcRequest.getArgs());
				// 封装返回结果
				rpcResponse.setData(result);
				rpcResponse.setDataType(method.getReturnType());
				rpcResponse.setMessage("ok");
			} catch (Exception e) {
				e.printStackTrace();
				rpcResponse.setMessage(e.getMessage());
				rpcResponse.setException(e);
			}
			doResponse(request,rpcResponse,serializer);

		});

	}

	void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
		HttpServerResponse response = request.response()
				.putHeader("content-type","application/json");

		try {
			byte[] serialized = serializer.serialize(rpcResponse);
			response.end(Buffer.buffer(serialized));
		} catch (IOException e) {
			e.printStackTrace();
			response.end(Buffer.buffer());
		}

	}
}
