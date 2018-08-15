package com.ibm.cloud_garage.http;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class HttpConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int MILLISECONDS_PER_MINUTE = 60 * MILLISECONDS_PER_SECOND;

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        HeaderElementIterator it = buildHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));

        while (it.hasNext()) {
            HeaderElement he = it.nextElement();
            String param = he.getName();
            String value = he.getValue();

            if (isNonNullTimeoutParamValue(param, value)) {
                try {
                    return Long.parseLong(value) * MILLISECONDS_PER_SECOND;
                } catch (NumberFormatException ignore) {
                    // do nothing
                }
            }
        }

        return MILLISECONDS_PER_MINUTE;
    }

    protected HeaderElementIterator buildHeaderElementIterator(HeaderIterator headerIterator) {
        return new BasicHeaderElementIterator(headerIterator);
    }

    protected boolean isNonNullTimeoutParamValue(String param, String value) {
        return (value != null && "timeout".equalsIgnoreCase(param));
    }
}