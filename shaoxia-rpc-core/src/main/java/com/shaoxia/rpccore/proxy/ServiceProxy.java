package com.shaoxia.rpccore.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.config.RegistryConfig;
import com.shaoxia.rpccore.config.RpcConfig;
import com.shaoxia.rpccore.constant.RpcConstant;
import com.shaoxia.rpccore.fault.retry.RetryStrategy;
import com.shaoxia.rpccore.fault.retry.RetryStrategyFactory;
import com.shaoxia.rpccore.fault.tolerant.TolerantStrategy;
import com.shaoxia.rpccore.fault.tolerant.TolerantStrategyFactory;
import com.shaoxia.rpccore.loadbalancer.LoadBalancer;
import com.shaoxia.rpccore.loadbalancer.LoadBalancerFactory;
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
import java.util.HashMap;
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


		RpcConfig rpcConfig = RpcApplication.getRpcConfig();

		// 构造请求
		String serviceName = method.getDeclaringClass().getName();
		RpcRequest rpcRequest = RpcRequest.builder()
				.serviceVersion(RpcConstant.DEFAULT_SERVICE_VERSION)
				.serviceName(serviceName)
				.methodName(method.getName())
				.parameterTypes(method.getParameterTypes())
				.args(args)
				.build();

		System.out.println("构造的请求为"+rpcRequest.toString());


		// 发送请求
		RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
		// 获取注册中心 默认ETCD
		Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
		ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
		serviceMetaInfo.setServiceName(serviceName);
		serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
		List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
		System.out.println("服务列表为"+serviceMetaInfos);
		if(CollUtil.isEmpty(serviceMetaInfos)){
			throw new RuntimeException("暂无服务地址");
		}
		System.out.println("开始负载均衡");
		LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
		// 将调用的方法名作为负载均衡参数
		String methodName = rpcRequest.getMethodName();
		HashMap<String, Object> requestParams = new HashMap<>();
		requestParams.put("methodName",methodName);
		ServiceMetaInfo selected = loadBalancer.select(requestParams, serviceMetaInfos);
		System.out.println("选择了"+selected);
		// 发送TCP请求
		try {
			RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
			RpcResponse rpcResponse = retryStrategy.doRetry(()->
				 VertxTcpClient.doRequest(rpcRequest, selected)
			);
			return rpcResponse.getData();
		}catch (Exception e) {
			TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(RpcApplication.getRpcConfig().getTolerantStrategy());
			tolerantStrategy.doTolerant(null,e);
		}

		return null;
	}
}
