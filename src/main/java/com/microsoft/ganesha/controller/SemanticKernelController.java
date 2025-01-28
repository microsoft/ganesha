package com.microsoft.ganesha.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.ganesha.exception.SemanticKernelException;
import com.microsoft.ganesha.semantickernel.Prompt;
import com.microsoft.ganesha.semantickernel.SemanticKernel;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
 

@RestController
public class SemanticKernelController {
    
    @Value("${AZURE_CLIENT_KEY}")
    private String azureClientKey;

    @Value("${CLIENT_ENDPOINT}")
    private String clientEndpoint;

    @Value("${MODEL_ID}")
    private String modelId;

    @Value("${AZURE_CLIENT_ID}")
    private String azureClientId;

    @Value("${AZURE_TENANT_ID}")
    private String azureTenantId;

    @Value("${AZURE_CLIENT_SECRET}")
    private String azureClientSecret;

    @Value("${AZURE_PROJECT_ID}")
    private String azureProjectId;

    @GetMapping("/lightstatus")
    String test() throws SemanticKernelException, ServiceNotFoundException {

        SemanticKernel sk = new SemanticKernel(azureClientId, azureTenantId, azureClientSecret, clientEndpoint, modelId, azureProjectId);
        return sk.GetSKResult("List the lights and if they are off or on.");
    }
    
    @PostMapping("/prompt")
    String replaceEmployee(@RequestBody Prompt prompt) throws SemanticKernelException, ServiceNotFoundException {
    
        SemanticKernel sk = new SemanticKernel(azureClientId, azureTenantId, azureClientSecret, clientEndpoint, modelId, azureProjectId);
        return sk.GetSKResult(prompt.getPrompt());
  }
}