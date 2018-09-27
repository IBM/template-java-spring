package com.ibm.hello.service;

import static java.lang.String.format;

import org.springframework.stereotype.Service;

import com.ibm.hello.model.GreetingResponse;

@Service(ServiceNameConstants.HOLA_NAME)
public class HolaService implements GreetingService {
    private static final String TEMPLATE = "Hola, %s.";

    @Override
    public GreetingResponse getGreeting(String name) {
        if (name == null) {
            return new GreetingResponse()
                    .withGreeting("Hola a todos.");
        }

        return new GreetingResponse()
                .withName(name)
                .withGreeting(format(TEMPLATE, name));
    }
}
