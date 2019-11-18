package com.ibm.inventory_management.config;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CloudantMapping implements Serializable {
    @JsonProperty(value = "CLOUDANT_CONFIG")
    private String cloudantConfig;
    @JsonProperty(value = "DATABASE_NAME")
    private String databaseName;

    public String getCloudantConfig() {
        return cloudantConfig;
    }

    public void setCloudantConfig(String cloudantConfig) {
        this.cloudantConfig = cloudantConfig;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
