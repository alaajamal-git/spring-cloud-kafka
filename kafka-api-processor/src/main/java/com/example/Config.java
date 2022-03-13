package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Configuration
public class Config {
	  
	private ExecutorService scheduler =
		     Executors.newFixedThreadPool(10);
	private ExecutorService scheduler2 =
		     Executors.newFixedThreadPool(10);
	 private volatile static long p = 0L;
	 
	 AtomicLong msg1 = new AtomicLong(0L);
	 AtomicLong msg2 = new AtomicLong(0L);
	 //private static final Logger log = LoggerFactory.getLogger(Config.class);

	
	 @Bean
     public Function<Flux<Message<ClientMessage>>, Flux<Message<ClientMessage>>> toDecision() {
         return payload -> payload.publishOn(Schedulers.fromExecutor(scheduler)).filter(msg -> msg.getPayload().getId()==1).doOnNext(x->System.out.println("toDecision"+ msg1.incrementAndGet())).map(msg -> MessageBuilder.withPayload(msg.getPayload())
           .setHeader("partitionKey", (p++)% 3)
           .build());   
         }

	 @Bean
     public Function<Flux<Message<ClientMessage>>, Flux<Message<ClientMessage>>> toLog() {
		 return payload -> payload.publishOn(Schedulers.fromExecutor(scheduler2)).doOnNext(x->System.out.println("toLog " + msg2.incrementAndGet())).map(msg -> MessageBuilder.withPayload(msg.getPayload())
				   .setHeader("partitionKey", (p++)% 3)
		           .build()); 
         }

}
	

