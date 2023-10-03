package com.maveric.csp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient
@SpringBootApplication
public class CspApplication {
	public static void main(String[] args) {
		SpringApplication.run(CspApplication.class, args);
	}
}
