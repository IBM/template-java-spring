package com.ibm.hello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.hello.service.HelloService;

@RestController
public class HelloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private HelloService service;

    public HelloController() {
        super();
    }

    @GetMapping("/hello/{name}")
    public String helloWorld(@PathVariable("name") final String name) {

        LOGGER.debug("Processing name: " + name);

        return service.createReply(name);
    }
}
