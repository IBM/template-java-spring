package com.ibm.hello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.hello.model.GreetingResponse;
import com.ibm.hello.service.HelloService;

@RestController
public class HelloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private HelloService service;

    public HelloController() {
        super();
    }

    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GreetingResponse> helloWorld() {
        LOGGER.info("No name provided");

        return ResponseEntity.status(406).build();
    }

    @GetMapping(value = "/hello/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GreetingResponse helloWorld(@PathVariable("name") final String name) {

        LOGGER.debug("Processing name: " + name);

        return service.getGreeting(name);
    }
}
