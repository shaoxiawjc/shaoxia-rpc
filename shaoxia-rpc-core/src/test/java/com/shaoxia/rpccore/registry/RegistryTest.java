package com.shaoxia.rpccore.registry;

import com.shaoxia.rpccore.config.RegistryConfig;
import com.shaoxia.rpccore.model.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-06-28 14:05
 */
public class RegistryTest {

	final Registry registry = new EtcdRegistry();

	@Before
	public void init(){
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setRegistryAddress("http://127.0.0.1:2379");
		registry.init(registryConfig);
	}

	@Test
	public void register() throws Exception{
		ServiceMetaInfo info1 = new ServiceMetaInfo();
		info1.setServiceVersion("1.0");
		info1.setServiceName("myService");
		info1.setServiceHost("localhost");
		info1.setServicePort(1234);
		registry.register(info1);
		ServiceMetaInfo info2 = new ServiceMetaInfo();
		info2.setServiceVersion("1.0");
		info2.setServiceName("myService");
		info2.setServiceHost("localhost");
		info2.setServicePort(1235);
		registry.register(info2);
		ServiceMetaInfo info3 = new ServiceMetaInfo();
		info3.setServiceVersion("2.0");
		info3.setServiceName("myService");
		info3.setServiceHost("localhost");
		info3.setServicePort(1235);
		registry.register(info3);
		Thread.sleep(60*1000);
	}

	@Test
	public void serviceDiscovery() throws InterruptedException {
		ServiceMetaInfo info = new ServiceMetaInfo();
		info.setServiceName("myService");
		info.setServiceVersion("1.0");
		String serviceKey = info.getServiceKey();
		List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceKey);

		Assert.assertNotNull(serviceMetaInfos);
	}





}
