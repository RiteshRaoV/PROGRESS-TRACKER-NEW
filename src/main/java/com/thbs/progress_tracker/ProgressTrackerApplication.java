package com.thbs.progress_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProgressTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgressTrackerApplication.class, args);
	}

}
