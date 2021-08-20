package com.ibm.cloud_native_toolkit.rest_template;

import java.util.Objects;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.StringUtils;

public interface RestTemplateProperties<T extends RestTemplateProperties<T>> {
    int DEFAULT_CONNECTIONS = 3;
    int DEFAULT_TIMEOUT = 100;

    String getProviderEndpoint();

    void setProviderEndpoint(String providerEndpoint);

    default T withProviderEndpoint(String providerEndpoint) {
        this.setProviderEndpoint(providerEndpoint);
        return (T) this;
    }

    int getMaxTotalConnections();

    void setMaxTotalConnections(int maxTotalConnections);

    default T withMaxTotalConnections(int maxTotalConnections) {
        this.setMaxTotalConnections(maxTotalConnections);
        return (T) this;
    }

    int getConnectTimeOutInSeconds();

    void setConnectTimeOutInSeconds(int connectTimeOutInSeconds);

    default T withConnectTimeOutInSeconds(int connectTimeOutInSeconds) {
        this.setConnectTimeOutInSeconds(connectTimeOutInSeconds);
        return (T) this;
    }

    int getSocketTimeOutInSeconds();

    void setSocketTimeOutInSeconds(int socketTimeOutInSeconds);

    default T withSocketTimeOutInSeconds(int socketTimeOutInSeconds) {
        this.setSocketTimeOutInSeconds(socketTimeOutInSeconds);
        return (T) this;
    }

    int getConnectionRequestTimeOutInSeconds();

    void setConnectionRequestTimeOutInSeconds(int connectionRequestTimeOutInSeconds);

    default T withConnectionRequestTimeOutInSeconds(int connectionRequestTimeOutInSeconds) {
        this.setConnectionRequestTimeOutInSeconds(connectionRequestTimeOutInSeconds);
        return (T) this;
    }

    default boolean isProxyRequired() {
        return StringUtils.hasText(getProxyHostname()) && getProxyPort() > 0;
    }

    String getProxyHostname();

    void setProxyHostname(String proxyHostname);

    default T withProxyHostname(String proxyHostname) {
        this.setProxyHostname(proxyHostname);
        return (T) this;
    }

    int getProxyPort();

    void setProxyPort(int proxyPort);

    default T withProxyPort(int proxyPort) {
        this.setProxyPort(proxyPort);
        return (T) this;
    }

    ClientHttpRequestInterceptor getLoggingInterceptor();

    void setLoggingInterceptor(ClientHttpRequestInterceptor loggingInterceptor);

    default T withLoggingInterceptor(ClientHttpRequestInterceptor loggingInterceptor) {
        this.setLoggingInterceptor(loggingInterceptor);
        return (T) this;
    }

    default boolean defaultEquals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestTemplateProperties)) {
            return false;
        }
        RestTemplateProperties that = (RestTemplateProperties) o;
        return getMaxTotalConnections() == that.getMaxTotalConnections()
                && getConnectTimeOutInSeconds() == that.getConnectTimeOutInSeconds()
                && getSocketTimeOutInSeconds() == that.getSocketTimeOutInSeconds()
                && getConnectionRequestTimeOutInSeconds() == that.getConnectionRequestTimeOutInSeconds()
                && getProxyPort() == that.getProxyPort()
                && Objects.equals(getProviderEndpoint(), that.getProviderEndpoint())
                && Objects.equals(getProxyHostname(), that.getProxyHostname())
                && Objects.equals(getLoggingInterceptor(), that.getLoggingInterceptor());
    }

    default int defaultHashCode() {
        return Objects.hash(
                getProviderEndpoint(),
                getMaxTotalConnections(),
                getConnectTimeOutInSeconds(),
                getSocketTimeOutInSeconds(),
                getConnectionRequestTimeOutInSeconds(),
                getProxyHostname(),
                getProxyPort(),
                getLoggingInterceptor());
    }

}
