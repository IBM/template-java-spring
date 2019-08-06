package com.ibm.hello.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ibm.hello.service.ServiceName;

@Configuration
@ConfigurationProperties(prefix = "greetingservice")
public class ServiceConfig {
    private ServiceName beanName;

    public ServiceName getBeanName() {
        return beanName;
    }

    public void setBeanName(ServiceName beanName) {
        this.beanName = beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = getServiceNameFromString(beanName);
    }

    protected static ServiceName getServiceNameFromString(String serviceName) {
        ServiceName result = ServiceName.safeValueOf(serviceName, null);
        if (result == null) {
            throw new IllegalStateException("Invalid service bean name used: " + serviceName);
        }

        return result;
    }
}
