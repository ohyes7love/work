package com.rok.seq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 시퀀스 채번과 GUID 채번을 위한 컨트롤러 클래스
 * 
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
@RestController
@CrossOrigin(origins="*", allowedHeaders = "*")
public class HelloController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/hello")
	public String getGuid() {
		
		logger.debug("TEST") ;

		return "Hello World!!!";
	}
	
	@GetMapping("/hello2")
	public String getHello2() {
		
		logger.debug("TEST") ;

		return "Hello World2!!!";
	}
}
