package com.microsoft.ganesha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.microsoft.applicationinsights.attach.ApplicationInsights;

@SpringBootApplication
public class GaneshaApplication {

	public static void main(String[] args) {
		//ApplicationInsights.attach();
		SpringApplication.run(GaneshaApplication.class, args);
	}

}
