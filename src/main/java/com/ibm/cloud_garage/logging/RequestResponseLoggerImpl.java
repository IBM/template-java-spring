package com.ibm.cloud_garage.logging;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

public class RequestResponseLoggerImpl implements RequestResponseLogger {
    private final Logger logger;
    private ObjectMapper objectMapper = new ObjectMapper();

    public RequestResponseLoggerImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void traceRequest(HttpRequest request, byte[] body) {

        if (logger.isInfoEnabled()) {

            try {
                logger.info("{}", beautifyContextString(new RequestLoggingContext()
                        .withUrl(Optional
                                .ofNullable(request.getURI())
                                .map(URI::toString)
                                .orElse(null))
                        .withMethod(Optional
                                .ofNullable(request.getMethod())
                                .map(HttpMethod::toString)
                                .orElse(null))
                        .withHeaders(request.getHeaders())
                        .withBody(getRequestBody(body))));
            } catch (IOException ex) {
                logger.warn("Error logging request", ex);
            }
        }
    }

    protected Object getRequestBody(byte[] body) {
        if (body != null && body.length > 0) {
            try {
                return objectMapper.readValue(body, Object.class);
            } catch (IOException e) {
                return new String(body, StandardCharsets.UTF_8);
            }
        } else {
            return null;
        }
    }

    @Override
    public void traceResponse(ClientHttpResponse response, HttpRequest request) {

        if (logger.isDebugEnabled()) {
            Optional<ClientHttpResponse> optionalResponse = Optional.ofNullable(response);

            try {
                logger.debug("{}", beautifyContextString(new ResponseLoggingContext()
                        .withUrl(Optional.ofNullable(request.getURI())
                                .map(URI::toString)
                                .orElse(null))
                        .withStatusCode(optionalResponse
                                .map(this::getStatusCode)
                                .map(HttpStatus::toString)
                                .orElse(null))
                        .withStatusText(optionalResponse
                                .map(this::getStatusText)
                                .orElse(null))
                        .withHeaders(optionalResponse
                                .map(ClientHttpResponse::getHeaders)
                                .orElse(null))
                        .withBody(getResponseBody(response))));
            } catch (IOException ex) {
                logger.warn("Error logging response", ex);
            }
        }
    }

    protected HttpStatus getStatusCode(ClientHttpResponse response) {
        try {
            return response.getStatusCode();
        } catch (IOException e) {
            return null;
        }
    }

    protected String getStatusText(ClientHttpResponse response) {
        try {
            return response.getStatusText();
        } catch (IOException e) {
            return null;
        }
    }

    protected Object getResponseBody(ClientHttpResponse response) {
        try {
            if (response != null) {
                return objectMapper.readValue(response.getBody(), Object.class);
            } else {
                return null;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e.toString());
            return null;
        }
    }

    protected String beautifyContextString(LoggingContext context) throws JsonProcessingException {
        return objectMapper.writeValueAsString(context);
    }
}
