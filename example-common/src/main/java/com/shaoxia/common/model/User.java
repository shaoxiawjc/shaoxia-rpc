package com.shaoxia.common.model;

import java.io.Serializable;

/**
 * @author wjc28
 * @version 1.0
 * @description: 用户实体类
 * @date 2024-06-23 19:03
 */
public class User implements Serializable {
	private String name;

	public User() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User(String name) {
		this.name = name;
	}
}
