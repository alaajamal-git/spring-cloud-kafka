package com.example;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@SpringBootApplication
public class SseClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SseClientApplication.class, args);
	}
	 @Bean
	    public WebClient webClient() {
		    	 ConnectionProvider provider = ConnectionProvider.builder("custom")
		        .maxConnections(50)
		        .pendingAcquireTimeout(Duration.ofMillis(60))
		        .maxIdleTime(Duration.ofMillis(30))
		        
		        .build();

	            HttpClient httpClient = HttpClient.create(provider);
	            return WebClient
	                    .builder()
	                    .baseUrl("http://aef16da5e28e44273ab020745b31eb8a-103246825.us-east-1.elb.amazonaws.com:8081")
	                    .defaultHeader("Cache-Control", "no-cache")
	                    .defaultHeader("Connection", "keep-alive")
	                    .clientConnector(new ReactorClientHttpConnector(httpClient))
	                    .build();
	        }
}
