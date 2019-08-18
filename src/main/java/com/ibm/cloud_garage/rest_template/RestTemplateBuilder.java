package com.ibm.cloud_garage.rest_template;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.ibm.cloud_garage.http.HttpConnectionKeepAliveStrategy;
import com.ibm.cloud_garage.logging.outbound.LoggingInterceptor;

public class RestTemplateBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateBuilder.class);

    static final int MILLISECONDS_PER_SECOND = 1000;

    public RestTemplate build(RestTemplateProperties restTemplateProperties) {
        RestTemplate restTemplate = new RestTemplate();

        if (restTemplateProperties != null) {
            restTemplate.setRequestFactory(buildClientHttpRequestFactory(restTemplateProperties));
            restTemplate.setInterceptors(buildInterceptors(restTemplateProperties.getLoggingInterceptor()));
        }

        return restTemplate;
    }

    protected ClientHttpRequestFactory buildClientHttpRequestFactory(RestTemplateProperties restTemplateProperties) {

        return new BufferingClientHttpRequestFactory(
                new HttpComponentsClientHttpRequestFactory(
                        buildHttpClient(restTemplateProperties)));
    }

    protected HttpClient buildHttpClient(RestTemplateProperties restTemplateProperties) {

        return HttpClientBuilder.create()
                .setKeepAliveStrategy(buildConnectionKeepAliveStrategy())
                .setConnectionManager(buildConnectionManager(restTemplateProperties))
                .setDefaultRequestConfig(buildRequestConfig(restTemplateProperties))
                .setProxy(buildProxySettings(restTemplateProperties))
                .build();
    }

    protected ConnectionKeepAliveStrategy buildConnectionKeepAliveStrategy() {
        return new HttpConnectionKeepAliveStrategy();
    }

    protected HttpClientConnectionManager buildConnectionManager(RestTemplateProperties restTemplateProperties) {
        PoolingHttpClientConnectionManager poolManager = new PoolingHttpClientConnectionManager();
        poolManager.setMaxTotal(restTemplateProperties.getMaxTotalConnections());
        poolManager.setDefaultMaxPerRoute(restTemplateProperties.getMaxTotalConnections());

        return poolManager;
    }

    protected RequestConfig buildRequestConfig(RestTemplateProperties restTemplateProperties) {
        return RequestConfig.custom()
                .setSocketTimeout(restTemplateProperties.getSocketTimeOutInSeconds() * MILLISECONDS_PER_SECOND)
                .setConnectTimeout(restTemplateProperties.getConnectTimeOutInSeconds() * MILLISECONDS_PER_SECOND)
                .setConnectionRequestTimeout(
                        restTemplateProperties.getConnectionRequestTimeOutInSeconds() * MILLISECONDS_PER_SECOND)
                .build();
    }

    protected HttpHost buildProxySettings(RestTemplateProperties restTemplateProperties) {
        return Optional.of(restTemplateProperties)
                .filter(RestTemplateProperties::isProxyRequired)
                .map(p -> new HttpHost(
                        restTemplateProperties.getProxyHostname(),
                        restTemplateProperties.getProxyPort()))
                .orElse(null);
    }

    protected List<ClientHttpRequestInterceptor> buildInterceptors(ClientHttpRequestInterceptor loggingInterceptor) {
        return Arrays.asList(Optional.ofNullable(loggingInterceptor).orElse(new LoggingInterceptor()));
    }

    public void close(RestTemplate restTemplate) {
        if (restTemplate != null) {
            ClientHttpRequestFactory requestFactory = restTemplate.getRequestFactory();
            if (requestFactory instanceof DisposableBean) {
                try {
                    ((DisposableBean) requestFactory).destroy();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }


}
