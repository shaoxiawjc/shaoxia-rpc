package com.shaoxia.springboot.consumer.service.impl;

import com.shaoxia.common.model.User;
import com.shaoxia.common.service.UserService;
import com.shaoxia.shaoxiarpcbootstarter.annotation.SXReference;
import org.springframework.stereotype.Service;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-07-07 19:55
 */
@Service
public class ExampleServiceImpl {
	@SXReference
	private UserService userService;

	public void example(){
		User user = new User();
		user.setName("game started");
		User user1 = userService.getUser(user);
		System.out.println(user1);
	}
}
