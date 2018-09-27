package com.ibm.hello.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ibm.hello.config.HelloConfig;
import com.ibm.hello.model.GreetingResponse;

@Service(ServiceNameConstants.HELLO_NAME)
public class HelloService implements GreetingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloService.class);

    @Autowired
    private HelloConfig config;

    @Override
    public GreetingResponse getGreeting(final String nameVariable) {

        final String name = !StringUtils.isEmpty(nameVariable) ? nameVariable : "world";

        LOGGER.debug("Creating reply for name: " + name);

        String greeting = String.format(config.getPattern(), name);

        return new GreetingResponse().withName(name).withGreeting(greeting);
    }
}
