package com.kartaca.airquality;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.kartaca.airquality.model")
public class AirqualityApplication {


	public static void main(String[] args) {
		SpringApplication.run(AirqualityApplication.class, args);
	}

}
