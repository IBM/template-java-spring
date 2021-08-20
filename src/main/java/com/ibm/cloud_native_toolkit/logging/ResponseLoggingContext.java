package com.ibm.cloud_native_toolkit.logging;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.http.HttpHeaders;

@JsonRootName(value = "ResponseLoggingContext")
@JsonPropertyOrder({ "url", "statusCode", "statusText", "headers", "body" })
public class ResponseLoggingContext implements LoggingContext {
    private String url;
    private String statusCode;
    private String statusText;
    private HttpHeaders headers;
    private Object body;

    public ResponseLoggingContext() {
        super();
    }

    public ResponseLoggingContext(ResponseLoggingContext context) {
        super();

        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }

        setUrl(context.getUrl());
        setStatusCode(context.getStatusCode());
        setStatusText(context.getStatusText());
        setHeaders(context.getHeaders());
        setBody(context.getBody());
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseLoggingContext withStatusCode(String statusCode) {
        this.setStatusCode(statusCode);
        return this;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public ResponseLoggingContext withStatusText(String statusText) {
        this.setStatusText(statusText);
        return this;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public ResponseLoggingContext withHeaders(HttpHeaders headers) {
        this.setHeaders(headers);
        return this;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public ResponseLoggingContext withBody(Object body) {
        this.setBody(body);
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResponseLoggingContext withUrl(String url) {
        this.setUrl(url);
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
        ResponseLoggingContext that = (ResponseLoggingContext) o;
        return Objects.equals(url, that.url)
                && Objects.equals(statusCode, that.statusCode)
                && Objects.equals(statusText, that.statusText)
                && Objects.equals(headers, that.headers)
                && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {

        return Objects.hash(url, statusCode, statusText, headers, body);
    }
}
