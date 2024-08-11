package com.shaoxia.springboot.provider;

import com.shaoxia.shaoxiarpcbootstarter.annotation.EnableSX;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSX
public class ExampleSpringbootProviderApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExampleSpringbootProviderApplication.class, args);
	}
}
