package com.ibm.hello.model;

public class GreetingResponse {
    private String name;
    private String greeting;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GreetingResponse withName(String name) {
        this.setName(name);
        return this;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public GreetingResponse withGreeting(String greeting) {
        this.setGreeting(greeting);
        return this;
    }
}
