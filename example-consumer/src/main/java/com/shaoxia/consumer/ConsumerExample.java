package com.shaoxia.consumer;

import com.shaoxia.common.model.User;
import com.shaoxia.common.service.UserService;
import com.shaoxia.rpccore.RpcApplication;
import com.shaoxia.rpccore.bootstrap.ConsumerBootstrap;
import com.shaoxia.rpccore.config.RpcConfig;
import com.shaoxia.rpccore.proxy.ServiceProxyFactory;

import java.util.Objects;

/**
 * @author wjc28
 * @version 1.0
 * @description: 消费者示例
 * @date 2024-06-23 19:19
 */
public class ConsumerExample {
	public static void main(String[] args) {
		ConsumerBootstrap.init();
		UserService userService = ServiceProxyFactory.getProxy(UserService.class);
		User user = new User();
		user.setName("shaoxia");

		User newUser = userService.getUser(user);
		System.out.println(newUser);

		if (Objects.isNull(newUser)){
			System.out.println("new user == null");
		}else {
			System.out.println("newUser: "+newUser);
		}
	}
}
