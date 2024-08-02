package com.shaoxia.springboot.consumer.service.impl;

import com.shaoxia.springboot.consumer.ExampleSpringbootConsumerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest(classes = ExampleSpringbootConsumerApplication.class)
class ExampleServiceImplTest {
	@Resource
	private ExampleServiceImpl exampleService;;

	@Test
	void example() {
		exampleService.example();
	}
}