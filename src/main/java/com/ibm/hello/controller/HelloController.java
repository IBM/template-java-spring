package com.ibm.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.hello.service.HelloService;

@RestController
public class HelloController {
    @Autowired
    private HelloService service;

    public HelloController() {
        super();
    }

    @RequestMapping("/hello/{name}")
    public String helloWorld(@PathVariable("name") final String name) {

        return service.createReply(name);
    }
}
