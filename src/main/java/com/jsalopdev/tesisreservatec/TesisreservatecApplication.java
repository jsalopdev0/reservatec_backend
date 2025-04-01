package com.jsalopdev.tesisreservatec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling

@SpringBootApplication
public class TesisreservatecApplication {

	public static void main(String[] args) {
		SpringApplication.run(TesisreservatecApplication.class, args);
	}

}
