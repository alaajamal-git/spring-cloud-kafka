package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import com.mongodb.reactivestreams.client.MongoClient;

@Configuration
public class MongoConfig {

	@Autowired
	MongoClient client;

	@Bean
	public ReactiveMongoTemplate reactiveMongoTemplate() {
		return new ReactiveMongoTemplate(client,"logs");
	}
}