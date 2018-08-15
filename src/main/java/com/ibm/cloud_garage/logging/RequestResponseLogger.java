package com.ibm.cloud_garage.logging;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

public interface RequestResponseLogger {
    void traceRequest(HttpRequest request, byte[] body);

    void traceResponse(ClientHttpResponse response, HttpRequest request);
}
