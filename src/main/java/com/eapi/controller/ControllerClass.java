package com.eapi.controller;


import com.eapi.getaccesstoken.GetAccessToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Getter
@Setter
@NoArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api")
public class ControllerClass {

    // private static Logger LOGGER = LogManager.getLogger(ControllerClass.class);

    @GetMapping
    public String hello(){
        return "Hello world";
    }

    @Autowired
    private GetAccessToken getAccessToken;

    @GetMapping("/token")
    public String getToken() {
        String startGateToken = getAccessToken.getToken();
        return startGateToken;
    }
}
