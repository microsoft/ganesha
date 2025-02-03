package com.microsoft.ganesha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.microsoft.ganesha")
@EnableAsync
public class GaneshaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaneshaApplication.class, args);
	}

}
