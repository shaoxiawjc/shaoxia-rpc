package com.shaoxia.springboot.consumer.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class ExampleServiceImplTest {
	@Resource
	private ExampleServiceImpl exampleService;;

	@Test
	void example() {
		exampleService.example();
	}
}