package com.shaoxia.rpceasy.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.shaoxia.rpceasy.model.RpcRequest;
import com.shaoxia.rpceasy.model.RpcResponse;
import com.shaoxia.rpceasy.serializer.JdkSerializer;
import com.shaoxia.rpceasy.serializer.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author wjc28
 * @version 1.0
 * @description: JDK服务代理
 * @date 2024-06-25 12:18
 */
public class ServiceProxy implements InvocationHandler {

	/**
	 * 调用代理
	 * @param proxy the proxy instance that the method was invoked on
	 *
	 *
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("invoke method"+method.getName());
		System.out.println("invoke object"+method.getDeclaringClass().getName());

		// 指定序列化器
		Serializer serializer = new JdkSerializer();

		// 构造请求
		RpcRequest rpcRequest = RpcRequest.builder()
				.serviceName(method.getDeclaringClass().getName())
				.methodName(method.getName())
				.parameterTypes(method.getParameterTypes())
				.args(args)
				.build();

		System.out.println("构造的请求为"+rpcRequest.toString());

		try {
			// 序列化
			byte[] bodyBytes = serializer.serialize(rpcRequest);
			// 发送请求
			// todo 使用注册中心服务发现来修改地址
			try (HttpResponse httpResponse= HttpRequest.post("localhost:8080")
					.body(bodyBytes)
					.execute()){
				byte[] result = httpResponse.bodyBytes();
				// 反序列化
				RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
				return  rpcResponse.getData();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
