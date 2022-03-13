package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
@RestController
@Configuration
public class Config {
	
	@Autowired
	StorageService service;
	private ExecutorService scheduler =
		     Executors.newFixedThreadPool(15);
	 AtomicLong msg = new AtomicLong(0L);
	private static volatile long p = 0L;
	
	 @Bean
     public Function<Flux<Message<ClientMessage>>, Flux<Message<ReplyMessage>>> fromFileUpdateToReplay() {
         return payload -> payload.publishOn(Schedulers.fromExecutor(scheduler)).map(msg -> {        	
        	 return MessageBuilder.withPayload(new ReplyMessage(msg.getPayload().getId(),service.featchFile(msg.getPayload().getText()), msg.getPayload().getDate(),0L))
        			   .setHeader("partitionKey", (p++)% 3)
        	           .build();
         })
	   			  .doOnNext(m -> System.out.println("msg "+msg.incrementAndGet())) .subscribeOn(Schedulers.parallel());   
         }
	
	
	
}
