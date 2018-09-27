package com.ibm.hello.service;

import com.ibm.hello.model.GreetingResponse;

public interface GreetingService {
    GreetingResponse getGreeting(String nameVariable);
}
