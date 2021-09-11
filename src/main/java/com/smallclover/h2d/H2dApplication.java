package com.smallclover.h2d;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = R2dbcAutoConfiguration.class)
public class H2dApplication {

	public static void main(String[] args) {
		SpringApplication.run(H2dApplication.class, args);
	}

}
