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

    @GetMapping("/lightstatus")
    String test() throws SemanticKernelException, ServiceNotFoundException {

        SemanticKernel sk = new SemanticKernel(AZURE_CLIENT_ID, AZURE_TENANT_ID, AZURE_CLIENT_SECRET, CLIENT_ENDPOINT, MODEL_ID);
        return sk.GetSKResult("List the lights and if they are off or on.");
    }
    
    @PostMapping("/prompt")
    String replaceEmployee(@RequestBody Prompt prompt) throws SemanticKernelException, ServiceNotFoundException {
    
        SemanticKernel sk = new SemanticKernel(AZURE_CLIENT_ID, AZURE_TENANT_ID, AZURE_CLIENT_SECRET, CLIENT_ENDPOINT, MODEL_ID);
        return sk.GetSKResult(prompt.getPrompt());
  }
}