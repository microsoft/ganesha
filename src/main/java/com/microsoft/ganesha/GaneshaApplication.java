package com.microsoft.ganesha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
	"com.microsoft.ganesha", 
	"com.eapi.helper",  
	"com.eapi.response"})
public class GaneshaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaneshaApplication.class, args);
	}

}
