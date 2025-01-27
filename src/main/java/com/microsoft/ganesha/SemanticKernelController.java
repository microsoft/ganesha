package com.microsoft.ganesha;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SemanticKernelController {
    
    @RequestMapping("/sktest")
    public String test() {
        return "test";
    }
}