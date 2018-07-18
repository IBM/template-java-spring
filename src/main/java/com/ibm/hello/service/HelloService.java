package com.ibm.hello.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ibm.hello.config.HelloConfig;

@Service
public class HelloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloService.class);

    @Autowired
    private HelloConfig config;

    public String createReply(final String nameVariable) {

        final String name = !StringUtils.isEmpty(nameVariable) ? nameVariable : "world";

        LOGGER.debug("Creating reply for name: " + name);

        return String.format(config.getPattern(), name);
    }
}
