package com.ibm.inventory_management.services;

import java.net.MalformedURLException;
import java.net.URL;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.ibm.inventory_management.config.CloudantConfig;

@Component
public class CloudantApi {
    @Bean
    public CloudantClient buildCloudant(CloudantConfig config) throws CloudServicesException {
        System.out.println("Config: " + config);
        URL url = null;
        try {
            url = new URL(config.getUrl());
        } catch (MalformedURLException e) {
            throw new CloudServicesException("Invalid service URL specified", e);
        }

        return ClientBuilder
                .url(url)
                .username(config.getUsername())
                .password(config.getPassword())
                .build();
    }
}
