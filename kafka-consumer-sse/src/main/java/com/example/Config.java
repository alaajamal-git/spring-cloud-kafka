package com.example;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

@RestController
public class Config {
	
	private final  Sinks.Many<ServerSentEvent<ReplyMessage>> streamProcessor =  Sinks.many().multicast().onBackpressureBuffer();
	private final Logger log = LoggerFactory.getLogger(Config.class);
	 AtomicLong msg = new AtomicLong(0L);

	private ExecutorService scheduler =
		     Executors.newFixedThreadPool(50);	
	@GetMapping(path = "/client", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<ReplyMessage>> streamFlux() {
	    return streamProcessor.asFlux().share();   	     
	}
	@Bean
	public Consumer<Flux<Message<ReplyMessage>>> toClient() {
	   return flux -> flux
		                  .publishOn(Schedulers.fromExecutor(scheduler))
			   			  .map(x->{x.getPayload().setRecieverDate(System.currentTimeMillis());return x;})
			   			  .flatMap(msg-> Mono.just(ServerSentEvent.<ReplyMessage> builder().id(UUID.randomUUID().toString()).event("test-event").retry(Duration.ofSeconds(3)).data(msg.getPayload()).build())
			   		      .doOnNext(this.streamProcessor::tryEmitNext))
			   			  .onErrorContinue((err, i) -> {log.info("onErrorContinue={}", "EEEEEEEEEEEEEror");})
			   			 // .log()
			   			  .subscribe(x-> System.out.print("msg "+msg.incrementAndGet()));
	}
	
	
}
