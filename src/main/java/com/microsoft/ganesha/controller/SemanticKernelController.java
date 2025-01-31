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
    public SemanticKernelController(SemanticKernel kernel, MongoService mongoService) {
        _kernel = kernel;
        _mongoService = mongoService;
    }

    private SemanticKernel _kernel;
    private MongoService _mongoService;
    
    @PostMapping("/prompt")
    String replaceEmployee(@RequestBody Prompt prompt) throws SemanticKernelException, ServiceNotFoundException {
        return _kernel.GetSKResult(prompt.getPrompt());
    }

    @PostMapping("/predictReason")
    String predictReason(@RequestBody MemberId memberid) throws SemanticKernelException, ServiceNotFoundException {
        return _kernel.GetReasons(memberid.GetMemberId());
    }
    
        SemanticKernel sk = new SemanticKernel(config);
        return sk.GetReasons(memberid.GetMemberId());
    }
  }
