package com.shaoxia.rpccore.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.config.RegistryConfig;
import com.shaoxia.rpccore.config.RpcConfig;
import com.shaoxia.rpccore.constant.RpcConstant;
import com.shaoxia.rpccore.model.RpcRequest;
import com.shaoxia.rpccore.model.RpcResponse;
import com.shaoxia.rpccore.model.ServiceMetaInfo;
import com.shaoxia.rpccore.protocol.*;
import com.shaoxia.rpccore.registry.Registry;
import com.shaoxia.rpccore.registry.RegistryFactory;
import com.shaoxia.rpccore.registry.RegistryKeys;
import com.shaoxia.rpccore.serializer.JdkSerializer;
import com.shaoxia.rpccore.serializer.Serializer;
import com.shaoxia.rpccore.serializer.SerializerFactory;
import com.shaoxia.rpccore.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author wjc28
 * @version 1.0
 * @description: JDK服务代理
 * @date 2024-06-25 12:18
 */
@Slf4j
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
		log.info("invoke method is {}",method.getName());
		log.info("invoke object is {}",method.getDeclaringClass().getName());

		// 指定序列化器
		RpcConfig rpcConfig = RpcApplication.getRpcConfig();
		final Serializer serializer = SerializerFactory.getInstance(rpcConfig.getSerializer());

		// 构造请求
		String serviceName = method.getDeclaringClass().getName();
		RpcRequest rpcRequest = RpcRequest.builder()
				.serviceName(serviceName)
				.methodName(method.getName())
				.parameterTypes(method.getParameterTypes())
				.args(args)
				.build();

		System.out.println("构造的请求为"+rpcRequest.toString());

		try {
			// 序列化
			byte[] bodyBytes = serializer.serialize(rpcRequest);
			// 发送请求
			RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
			// 获取注册中心 默认ETCD
			Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
			ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
			serviceMetaInfo.setServiceName(serviceName);
			serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
			List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
			if(CollUtil.isEmpty(serviceMetaInfos)){
				throw new RuntimeException("暂无服务地址");
			}

			ServiceMetaInfo selected = serviceMetaInfos.get(0);
			// 发送TCP请求
			RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selected);
			return rpcResponse.getData();

		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
