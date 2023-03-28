package com.rok.seq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping({ "/webfluxTest" })
public class WebfluxTestController {
	Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping("/hello")
    public Mono<String> hello() {
		logger.info("Hello, World!!!!!!!!!!!!!!!") ;
        return Mono.just("Hello, World!");
    }
}
