package com.ibm.hello.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.hello.model.GreetingRequest;
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
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Name parameter missing")
    })
    public ResponseEntity<GreetingResponse> helloWorld(
            @RequestParam(name = "name", required = false) final String name
    ) {

        LOGGER.debug("Processing name: " + name);

        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.status(406).build();
        }

        return ResponseEntity.ok(service.getGreeting(name));
    }

    @PostMapping(
            value = "/hello",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Content body is missing"),
            @ApiResponse(code = 406, message = "Name parameter missing"),
            @ApiResponse(code = 415, message = "Missing content type")
    })
    public ResponseEntity<GreetingResponse> helloWorld(
            @RequestBody GreetingRequest request
    ) {

        LOGGER.debug("Processing name: " + request.getName());

        if (StringUtils.isEmpty(request.getName())) {
            return ResponseEntity.status(406).build();
        }

        return ResponseEntity.ok(service.getGreeting(request.getName()));
    }
}
