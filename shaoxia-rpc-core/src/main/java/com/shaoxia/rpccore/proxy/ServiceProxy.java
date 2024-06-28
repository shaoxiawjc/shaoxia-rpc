package com.shaoxia.rpccore.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.config.RegistryConfig;
import com.shaoxia.rpccore.config.RpcConfig;
import com.shaoxia.rpccore.constant.RpcConstant;
import com.shaoxia.rpccore.model.RpcRequest;
import com.shaoxia.rpccore.model.RpcResponse;
import com.shaoxia.rpccore.model.ServiceMetaInfo;
import com.shaoxia.rpccore.registry.Registry;
import com.shaoxia.rpccore.registry.RegistryFactory;
import com.shaoxia.rpccore.registry.RegistryKeys;
import com.shaoxia.rpccore.serializer.JdkSerializer;
import com.shaoxia.rpccore.serializer.Serializer;
import com.shaoxia.rpccore.serializer.SerializerFactory;
import com.sun.jmx.mbeanserver.Repository;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

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
			// todo 使用注册中心服务发现来修改地址
			RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
			Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
			ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
			serviceMetaInfo.setServiceName(serviceName);
			serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
			List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
			if(CollUtil.isEmpty(serviceMetaInfos)){
				throw new RuntimeException("暂无服务地址");
			}
			ServiceMetaInfo selected = serviceMetaInfos.get(0);
			try (HttpResponse httpResponse= HttpRequest.post(selected.getServiceAddress())
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
