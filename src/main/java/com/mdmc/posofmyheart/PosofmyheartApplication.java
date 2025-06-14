package com.mdmc.posofmyheart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PosofmyheartApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosofmyheartApplication.class, args);
	}

}
