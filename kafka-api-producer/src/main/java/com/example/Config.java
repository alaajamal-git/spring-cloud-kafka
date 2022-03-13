package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

@RestController
public class Config {
	private ExecutorService scheduler =
		     Executors.newFixedThreadPool(10);	
	
	 //private static final Logger log = LoggerFactory.getLogger(Config.class);
	 private final  Sinks.Many<ClientMessage> streamProcessor =  Sinks.many().multicast().onBackpressureBuffer();

	 private volatile static long p = 0L;
	 AtomicLong msg = new AtomicLong(0L);

	 @PostMapping("/test")
	 public Mono<ResponseEntity<?>> endPoint1(@RequestBody ClientMessage msg){
		 streamProcessor.tryEmitNext(msg);
		return Mono.just(new ResponseEntity<>(HttpStatus.ACCEPTED));
	 }
	 @Bean
	    public Supplier<Flux<Message<ClientMessage>>> toRequest() {
	        return () -> streamProcessor.asFlux()
	        		.publishOn(Schedulers.fromExecutor(scheduler))
	        		.map(msg -> MessageBuilder.withPayload(msg)
	        				.setHeader("partitionKey", (p++)% 3) 
	        				.build())
	        		.doOnNext(m -> System.out.println("msg "+msg.incrementAndGet()))
	        	    ;
	    }

}
	

