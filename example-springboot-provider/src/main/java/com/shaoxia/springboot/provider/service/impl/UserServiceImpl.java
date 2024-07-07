package com.shaoxia.springboot.provider.service.impl;

import com.shaoxia.common.model.User;
import com.shaoxia.common.service.UserService;
import com.shaoxia.shaoxiarpcbootstarter.annotation.SXService;
import org.springframework.stereotype.Service;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-07-07 19:52
 */
@SXService
@Service
public class UserServiceImpl implements UserService {
	@Override
	public User getUser(User user) {
		System.out.println("springboot UserServiceImpl");
		user.setName("GameOver");
		return user;
	}
}
