package com.microsoft.ganesha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.exception.SemanticKernelException;
import com.microsoft.ganesha.semantickernel.MemberId;
import com.microsoft.ganesha.semantickernel.Prompt;
import com.microsoft.ganesha.semantickernel.SemanticKernel;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
 

@RestController
public class SemanticKernelController {
    
    @Autowired
    private AppConfig config;

    // @GetMapping("/lightstatus")
    // String test() throws SemanticKernelException, ServiceNotFoundException {

    //     SemanticKernel sk = new SemanticKernel(config);
    //     return sk.GetSKResult("List the lights and if they are off or on.");
    // }
    
    @PostMapping("/prompt")
    String replaceEmployee(@RequestBody Prompt prompt) throws SemanticKernelException, ServiceNotFoundException {
    
        SemanticKernel sk = new SemanticKernel(config);
        return sk.GetSKResult(prompt.getPrompt());
    }

    @PostMapping("/predictReason")
    String predictReason(@RequestBody MemberId memberid) throws SemanticKernelException, ServiceNotFoundException {
    
        SemanticKernel sk = new SemanticKernel(config);
        return sk.GetReasons(memberid.GetMemberId());
    }
  }
