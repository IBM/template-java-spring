package com.ibm.cloud_garage.logging.inbound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class ClientHttpResponseWrapper implements ClientHttpResponse {
    private final ContentCachingResponseWrapper response;

    public ClientHttpResponseWrapper(ContentCachingResponseWrapper response) {
        this.response = response;
    }

    @Override
    public HttpStatus getStatusCode() throws IOException {
        return HttpStatus.resolve(response.getStatus());
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return response.getStatus();
    }

    @Override
    public String getStatusText() throws IOException {
        return getStatusCode().getReasonPhrase();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("close");
    }

    @Override
    public InputStream getBody() throws IOException {

        return new ByteArrayInputStream(response.getContentAsByteArray());
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        for (String headerName : response.getHeaderNames()) {
            for (String headerValue : response.getHeaders(headerName)) {
                headers.add(headerName, headerValue);
            }
        }

        return headers;
    }
}
