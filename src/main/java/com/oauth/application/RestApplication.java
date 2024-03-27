package com.oauth.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestApplication {
	public static void main(String[] args) {

		System.out.println("in main");
		SpringApplication.run(RestApplication.class, args);
	}

}
