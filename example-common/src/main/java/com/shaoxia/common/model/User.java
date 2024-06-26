package com.shaoxia.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjc28
 * @version 1.0
 * @description: 用户实体类
 * @date 2024-06-23 19:03
 */

public class User implements Serializable {
	private String name;

	private Integer age;

	private Date birthday;

	public User() {
	}

	public User(String name, Integer age, Date birthday) {
		this.name = name;
		this.age = age;
		this.birthday = birthday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", age=" + age +
				", birthday=" + birthday +
				'}';
	}
}
