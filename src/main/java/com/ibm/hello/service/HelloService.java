package com.ibm.hello.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HelloService {

    public String createReply(final String nameVariable) {

        final String name = !StringUtils.isEmpty(nameVariable) ? nameVariable : "world";

        return String.format("Hello, %s!", name);
    }
}
