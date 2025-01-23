package com.eapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ExperienceApiApplication {
	
	private static final Logger LOGGER = LogManager.getLogger(ExperienceApiApplication.class);
	
	@Bean
    public RestTemplate restTemplate()  {
        return new RestTemplate();
    }
	
	@Bean
	public HttpHeaders httpHeaders() {
		return new HttpHeaders();
	}
	
	public static void main(String[] args) {
		LOGGER.info("<------ Navigator Experience API : servicename ----->");
		SpringApplication.run(ExperienceApiApplication.class, args);
	}

}
