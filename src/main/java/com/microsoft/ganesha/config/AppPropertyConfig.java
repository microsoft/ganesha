package com.microsoft.ganesha.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Getter
@Setter
@Configuration
@ConfigurationProperties
public class AppPropertyConfig implements Serializable {

    private static final long serialVersionUID = -2943349621371897644L;

    @Value("${HemiAccessTokenEndpoint}")
    private String hemiAccessTokenEndpoint;

    @Value("${HemiAccessTokenRequest}")
    private String hemiAccessTokenRequest;

    @Value("${HemiAccessACQTokenRequest}")
    private String hemiAccessACQTokenRequest;

}
