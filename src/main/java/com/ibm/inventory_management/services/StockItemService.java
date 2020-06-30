package com.ibm.inventory_management.services;

import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import java.net.URL;
import java.net.MalformedURLException;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.Database;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ibm.inventory_management.config.CloudantConfig;
import com.ibm.inventory_management.models.StockItem;




//@Profile("!mock")

@Service
@Primary
public class StockItemService implements StockItemApi {
    @Bean
    public static CloudantClient buildCloudant(CloudantConfig config) throws CloudServicesException { 
        System.out.println("Config: " + config);
        URL url = null;
        try {
            url = new URL(config.getUrl());
        } catch (MalformedURLException e) {
            throw new CloudServicesException("Invalid service URL specified", e);
        }
       
       return ClientBuilder
                .url(url)
                .iamApiKey(config.getApikey())
                //.username(config.getUsername())
                //.password(config.getPassword())
                .build();
    }
    private CloudantConfig config;
    private CloudantClient client;
    private Database db = null;

    public StockItemService(CloudantConfig config, @Lazy CloudantClient client) {
        this.config = config;
        this.client = client;
    }

    @PostConstruct
    public void init() {
        db = client.database(config.getDatabaseName(), true);
    }

    @Override
    public List<StockItem> listStockItems() throws Exception {

        try {
            return db.getAllDocsRequestBuilder()
                    .includeDocs(true)
                    .build()
                    .getResponse()
                    .getDocsAs(StockItem.class);

        } catch (IOException e) {
            throw new Exception("", e);
        }
    }
}
