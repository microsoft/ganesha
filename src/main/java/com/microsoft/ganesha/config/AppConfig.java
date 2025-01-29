package com.microsoft.ganesha.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${AZURE_CLIENT_KEY}")
    private String AZURE_CLIENT_KEY;

    @Value("${CLIENT_ENDPOINT}")
    private String CLIENT_ENDPOINT;

    @Value("${MODEL_ID}")
    private String MODEL_ID;

    @Value("${AZURE_CLIENT_ID}")
    private String AZURE_CLIENT_ID;

    @Value("${AZURE_TENANT_ID}")
    private String AZURE_TENANT_ID;

    @Value("${AZURE_CLIENT_SECRET}")
    private String AZURE_CLIENT_SECRET;

    public String getAZURE_CLIENT_KEY() {
        return AZURE_CLIENT_KEY;
    }

    public String getCLIENT_ENDPOINT() {
        return CLIENT_ENDPOINT;
    }

    public String getMODEL_ID() {
        return MODEL_ID;
    }

    public String getAZURE_CLIENT_ID() {
        return AZURE_CLIENT_ID;
    }

    public String getAZURE_TENANT_ID() {
        return AZURE_TENANT_ID;
    }

    public String getAZURE_CLIENT_SECRET() {
        return AZURE_CLIENT_SECRET;
    }
}
