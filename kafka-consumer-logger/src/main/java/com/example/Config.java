package com.example;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.messaging.Message;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Configuration
public class Config {
	
	@Autowired
	ReactiveMongoTemplate template;
	private ScheduledExecutorService scheduler =
		     Executors.newScheduledThreadPool(10);
	private static volatile long msgn = 0L;

	@Bean
	public Consumer<Flux<Message<ClientMessage>>> toMongo() {
	   return flux -> flux.publishOn(Schedulers.fromExecutor(scheduler))
			   			  .doOnNext(m -> System.out.println("msg "+msgn++))
			   			  .onBackpressureBuffer()
			   			  .subscribe(new BaseSubscriber<Message<ClientMessage>>() {
			   	            @Override
			   	            protected void hookOnSubscribe(Subscription subscription) {
			   	                subscription.request(1000);
			   	            }

			   	            @Override
			   	            protected void hookOnNext(Message<ClientMessage> msg) {
			   	            	template.save(new MongoMessage(UUID.randomUUID().toString(),msg.toString())).subscribe();
			   	                
			   	            }
			   	        })
			              //.subscribe((msg)->{template.save(new MongoMessage(UUID.randomUUID().toString(),msg.toString())).subscribe();})
			              ;
			   			  
	}
	
	
}
	

