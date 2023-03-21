package com.rok.seq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RestSeqApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestSeqApiApplication.class, args);
	}

}
