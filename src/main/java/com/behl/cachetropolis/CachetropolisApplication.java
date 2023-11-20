package com.behl.cachetropolis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CachetropolisApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachetropolisApplication.class, args);
	}

}
