package com.microsoft.ganesha.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.exception.SemanticKernelException;
import com.microsoft.ganesha.helper.TokenHelper;
import com.microsoft.ganesha.rest.RestClient;
import com.microsoft.ganesha.semantickernel.MemberId;
import com.microsoft.ganesha.semantickernel.Prompt;
import com.microsoft.ganesha.semantickernel.SemanticKernel;
import com.microsoft.semantickernel.services.ServiceNotFoundException;

@RestController
public class SemanticKernelController {
    
    private final AppConfig config;
    private final TokenHelper tokenHelper;
    private final RestClient restClient;

    public SemanticKernelController(AppConfig config, TokenHelper tokenHelper, RestClient restClient) {
        this.config = config;
        this.tokenHelper = tokenHelper;
        this.restClient = restClient;
    }
    
    @PostMapping("/prompt")
    String replaceEmployee(@RequestBody Prompt prompt) throws SemanticKernelException, ServiceNotFoundException {
    
        SemanticKernel sk = new SemanticKernel(config , tokenHelper, restClient);
        return sk.GetSKResult(prompt.getPrompt());
    }

    @PostMapping("/predictReason")
    String predictReason(@RequestBody MemberId memberid) throws SemanticKernelException, ServiceNotFoundException {
    
        SemanticKernel sk = new SemanticKernel(config, tokenHelper, restClient);
        return sk.GetReasons(memberid.GetMemberId());
    }
  }
