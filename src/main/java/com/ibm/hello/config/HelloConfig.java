package com.ibm.hello.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hello")
public class HelloConfig {
    private String pattern;

    public HelloConfig() {
        super();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public HelloConfig withPattern(String pattern) {
        this.setPattern(pattern);
        return this;
    }

    public String toString() {
        return String.format("[HelloConfig: pattern=%s]", pattern);
    }
}
