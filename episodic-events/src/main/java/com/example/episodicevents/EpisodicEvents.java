package com.example.episodicevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EpisodicEvents {

	public static void main(String[] args) {
		SpringApplication.run(EpisodicEvents.class, args);
	}
}
