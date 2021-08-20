package com.ibm.cloud_native_toolkit.rest_template;

import org.springframework.http.client.ClientHttpRequestInterceptor;

public class SimpleRestTemplateProperties implements RestTemplateProperties<SimpleRestTemplateProperties> {

    private String providerEndpoint;
    private int maxTotalConnections = DEFAULT_CONNECTIONS;
    private int connectTimeOutInSeconds = DEFAULT_TIMEOUT;
    private int socketTimeOutInSeconds = DEFAULT_TIMEOUT;
    private int connectionRequestTimeOutInSeconds = DEFAULT_TIMEOUT;
    private String proxyHostname;
    private int proxyPort = 0;
    private ClientHttpRequestInterceptor loggingInterceptor;

    public SimpleRestTemplateProperties() {
        super();
    }

    public SimpleRestTemplateProperties(RestTemplateProperties properties) {
        super();

        setProviderEndpoint(properties.getProviderEndpoint());
        setMaxTotalConnections(properties.getMaxTotalConnections());
        setConnectTimeOutInSeconds(properties.getConnectTimeOutInSeconds());
        setSocketTimeOutInSeconds(properties.getSocketTimeOutInSeconds());
        setConnectionRequestTimeOutInSeconds(properties.getConnectionRequestTimeOutInSeconds());
        setProxyHostname(properties.getProxyHostname());
        setProxyPort(properties.getProxyPort());
        setLoggingInterceptor(properties.getLoggingInterceptor());
    }

    @Override
    public String getProviderEndpoint() {
        return providerEndpoint;
    }

    @Override
    public void setProviderEndpoint(String providerEndpoint) {
        this.providerEndpoint = providerEndpoint;
    }

    @Override
    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    @Override
    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    @Override
    public int getConnectTimeOutInSeconds() {
        return connectTimeOutInSeconds;
    }

    @Override
    public void setConnectTimeOutInSeconds(int connectTimeOutInSeconds) {
        this.connectTimeOutInSeconds = connectTimeOutInSeconds;
    }

    @Override
    public int getSocketTimeOutInSeconds() {
        return socketTimeOutInSeconds;
    }

    @Override
    public void setSocketTimeOutInSeconds(int socketTimeOutInSeconds) {
        this.socketTimeOutInSeconds = socketTimeOutInSeconds;
    }

    @Override
    public int getConnectionRequestTimeOutInSeconds() {
        return connectionRequestTimeOutInSeconds;
    }

    @Override
    public void setConnectionRequestTimeOutInSeconds(int connectionRequestTimeOutInSeconds) {
        this.connectionRequestTimeOutInSeconds = connectionRequestTimeOutInSeconds;
    }

    @Override
    public String getProxyHostname() {
        return proxyHostname;
    }

    @Override
    public void setProxyHostname(String proxyHostname) {
        this.proxyHostname = proxyHostname;
    }

    @Override
    public int getProxyPort() {
        return proxyPort;
    }

    @Override
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    @Override
    public ClientHttpRequestInterceptor getLoggingInterceptor() {
        return loggingInterceptor;
    }

    @Override
    public void setLoggingInterceptor(ClientHttpRequestInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public boolean equals(Object o) {
        return defaultEquals(o);
    }

    @Override
    public int hashCode() {
        return defaultHashCode();
    }
}
