package com.daniel.ms_restaurants;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsRestaurantsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsRestaurantsApplication.class, args);
	}

}
