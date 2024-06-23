package com.shaoxia.consumer;

import com.shaoxia.common.model.User;
import com.shaoxia.common.service.UserService;

import java.util.Objects;

/**
 * @author wjc28
 * @version 1.0
 * @description: 消费者示例
 * @date 2024-06-23 19:19
 */
public class ConsumerExample {
	public static void main(String[] args) {
		UserService userService = null;
		User user = new User();
		user.setName("shaoxia");

		User newUser = userService.getUser(user);

		if (Objects.isNull(newUser)){
			System.out.println("new user == null");
		}else {
			System.out.println("newUser name: "+newUser.getName());
		}
	}
}
