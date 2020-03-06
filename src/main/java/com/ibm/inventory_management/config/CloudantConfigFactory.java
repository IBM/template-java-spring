package com.ibm.inventory_management.config;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CloudantConfigFactory {
    @Bean
    public CloudantConfig buildCloudantConfig() throws IOException {
        return buildConfigFromBinding(
                loadCloudantConfig(),
                loadDatabaseName()
        );
    }

    protected String loadCloudantConfig() throws IOException {
        return System.getProperty("CLOUDANT_CONFIG") != null
                ? System.getProperty("CLOUDANT_CONFIG")
                : loadCloudantMappingFromLocalDev().getCloudantConfig();
    }

    protected CloudantMapping loadCloudantMappingFromLocalDev() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(
                this.getClass().getClassLoader().getResourceAsStream("mappings.json"),
                CloudantMapping.class
        );
    }

    protected String loadDatabaseName() throws IOException {
        return System.getProperty("DATABASE_NAME") != null
                ? System.getProperty("DATABASE_NAME")
                : loadCloudantMappingFromLocalDev().getDatabaseName();
    }

    protected CloudantConfig buildConfigFromBinding(String binding, String databaseName) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        if (binding == null) {
            return new CloudantConfig();
        }

        final CloudantConfig baseConfig = mapper.readValue(binding, CloudantConfig.class);

        if (baseConfig == null) {
            return new CloudantConfig();
        }

        return baseConfig.withDatabaseName(databaseName);
    }
}
