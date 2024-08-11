package com.shaoxia.springboot.consumer;

import com.shaoxia.shaoxiarpcbootstarter.annotation.EnableSX;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSX(needServer = false)
public class ExampleSpringbootConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleSpringbootConsumerApplication.class, args);
	}

}
