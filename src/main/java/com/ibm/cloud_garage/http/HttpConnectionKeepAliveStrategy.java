package com.ibm.cloud_garage.http;

import java.util.Objects;
import java.util.stream.Stream;

import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class HttpConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
    static final int MILLISECONDS_PER_SECOND = 1000;
    private static final long MILLISECONDS_PER_MINUTE = 60L * MILLISECONDS_PER_SECOND;
    static final long DEFAULT_KEEP_ALIVE_DURATION = MILLISECONDS_PER_MINUTE;

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {

        return Stream.of(response.getHeaders(HTTP.CONN_KEEP_ALIVE))
                .filter(Objects::nonNull)
                .flatMap(h -> Stream.of(h.getElements()))
                .filter(Objects::nonNull)
                .filter(he -> "timeout".equalsIgnoreCase(he.getName()))
                .map(HeaderElement::getValue)
                .map(this::getTimeoutInMilliseconds)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(DEFAULT_KEEP_ALIVE_DURATION);
    }

    protected Long getTimeoutInMilliseconds(String timeoutValue) {
        try {
            return Long.parseLong(timeoutValue) * MILLISECONDS_PER_SECOND;
        } catch (NumberFormatException ignore) {
            return null;
        }
    }
}