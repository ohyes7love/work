package com.rest.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class RestTestController {
	 
	@RequestMapping("/getTest")
	public String getTest() {   
		return "Hello World";
	}
}
