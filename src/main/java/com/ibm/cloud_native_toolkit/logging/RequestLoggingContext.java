package com.ibm.cloud_native_toolkit.logging;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.http.HttpHeaders;

@JsonRootName(value = "RequestLoggingContext")
@JsonPropertyOrder({ "url", "method", "headers", "body" })
public class RequestLoggingContext implements LoggingContext {
    private String url;
    private String method;
    private HttpHeaders headers;
    private Object body;

    public RequestLoggingContext() {
        super();
    }

    public RequestLoggingContext(RequestLoggingContext context) {
        super();

        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }

        setUrl(context.getUrl());
        setMethod(context.getMethod());
        setHeaders(context.getHeaders());
        setBody(context.getBody());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RequestLoggingContext withUrl(String url) {
        this.setUrl(url);
        return this;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public RequestLoggingContext withMethod(String method) {
        this.setMethod(method);
        return this;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public RequestLoggingContext withHeaders(HttpHeaders headers) {
        this.setHeaders(headers);
        return this;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public RequestLoggingContext withBody(Object body) {
        this.setBody(body);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestLoggingContext that = (RequestLoggingContext) o;
        return Objects.equals(url, that.url)
                && Objects.equals(method, that.method)
                && Objects.equals(headers, that.headers)
                && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {

        return Objects.hash(url, method, headers, body);
    }
}
