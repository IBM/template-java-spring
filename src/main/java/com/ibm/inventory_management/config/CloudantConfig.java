package com.ibm.inventory_management.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudantConfig {
    private String url;
    private String username;
    private String password;
    private String databaseName;
    private String apikey;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CloudantConfig withUrl(String url) {
        this.setUrl(url);

        return this;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CloudantConfig withUsername(String username) {
        this.setUsername(username);

        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CloudantConfig withPassword(String password) {
        this.setPassword(password);

        return this;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public CloudantConfig withDatabaseName(String databaseName) {
        this.setDatabaseName(databaseName);

        return this;
    }

    public String toString() {
        return "[CloudantConfig: url=" + this.url + ", username=" + this.username + ", name=" + this.databaseName + "]";
    }
}
