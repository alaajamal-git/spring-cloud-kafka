package com.example;

import java.time.Duration;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.LongStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class Controller {
	private static Logger logger = LoggerFactory.getLogger(Controller.class);
	@Autowired
    private WebClient webClient ;
	private ExecutorService scheduler =
		     Executors.newFixedThreadPool(20);
	
	private final Logger log = LoggerFactory.getLogger(Controller.class);
    List<ReplyMessage> results = new CopyOnWriteArrayList<ReplyMessage>();

    @GetMapping("/results")
    public Mono<ResponseEntity<String>> showPerformance() {
    	OptionalDouble rs = results.stream().flatMapToLong(msg -> LongStream.of(msg.getDate() - msg.getRecieverDate())).average();
    	if(!rs.isEmpty())
    	return Mono.just(new ResponseEntity<>("latancy: "+rs.getAsDouble()+" ms\n"+"number of events: "+results.size(),HttpStatus.OK)).doFinally((s) -> results.clear());
    	else return Mono.just(new ResponseEntity<>("empty",HttpStatus.OK));
    		
    }

    @Bean
    public void consumeSSE() {
        ParameterizedTypeReference<ServerSentEvent<ReplyMessage>> type = new ParameterizedTypeReference<ServerSentEvent<ReplyMessage>>() {
        };

        Flux<ServerSentEvent<ReplyMessage>> eventStream = webClient.get()
            .uri("/client")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .retrieve()  
            .bodyToFlux(type)
            .timeout(Duration.ofSeconds(3600))          
            .onErrorContinue((err, i) -> {log.info("onErrorContinue={}", "EEEEEEEEEEEEEror");});
            

        eventStream.publishOn(Schedulers.fromExecutor(scheduler)).subscribe(content -> 
        	{
        		ReplyMessage msg = content.data();
        		results.add(msg);
        		logger.info(".");
        	});
    }
    
   
}
