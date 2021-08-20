package com.ibm.cloud_native_toolkit.logging.outbound;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.ibm.cloud_native_toolkit.logging.RequestResponseLogger;
import com.ibm.cloud_native_toolkit.logging.RequestResponseLoggerImpl;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {
    private static Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    private final RequestResponseLogger delegate;

    public LoggingInterceptor() {
        this.delegate = new RequestResponseLoggerImpl(logger);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        ClientHttpResponse clientHttpResponse = null;
        try {
            delegate.traceRequest(request, body);
            clientHttpResponse = execution.execute(request, body);
        } finally {
            delegate.traceResponse(clientHttpResponse, request);
        }

        return clientHttpResponse;
    }
}
