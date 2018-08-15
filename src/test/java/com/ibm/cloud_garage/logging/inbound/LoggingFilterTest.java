package com.ibm.cloud_garage.logging.inbound;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.ibm.cloud_garage.logging.RequestResponseLogger;

@DisplayName("LoggingFilter")
public class LoggingFilterTest {
    private LoggingFilter loggingFilterSpy;
    private RequestResponseLogger loggerMock;

    @BeforeEach
    public void setup() {
        LoggingFilter original = new LoggingFilter();

        loggerMock = mock(RequestResponseLogger.class);
        ReflectionTestUtils.setField(original, "delegate", loggerMock);

        loggingFilterSpy = spy(original);
    }

    @Nested
    @DisplayName("Given doFilter()")
    public class GivenDoFilter {
        @Test
        @DisplayName("When called then it should call appropriate sequence")
        public void should_log_request_doFilter_and_logResponse() throws IOException, ServletException {
            ResettableHttpServletRequest resettableRequest = mock(ResettableHttpServletRequest.class);
            doReturn(resettableRequest).when(loggingFilterSpy).getResettableHttpServletRequest(any());

            ContentCachingResponseWrapper cachingResponse = mock(ContentCachingResponseWrapper.class);
            doReturn(cachingResponse).when(loggingFilterSpy).getContentCachingResponseWrapper(any());

            doNothing().when(loggingFilterSpy).logRequest(any());
            doNothing().when(loggingFilterSpy).logResponse(any(), any());

            HttpServletRequest requestMock = mock(HttpServletRequest.class);
            HttpServletResponse responseMock = mock(HttpServletResponse.class);
            FilterChain chainMock = mock(FilterChain.class);

            loggingFilterSpy.doFilter(requestMock, responseMock, chainMock);

            verify(loggingFilterSpy).logRequest(resettableRequest);
            verify(chainMock).doFilter(resettableRequest, cachingResponse);
            verify(loggingFilterSpy).logResponse(cachingResponse, resettableRequest);
            verify(cachingResponse).copyBodyToResponse();
        }
    }

    @Nested
    @DisplayName("Given logRequest()")
    public class GivenLogRequest {
        @Test
        @DisplayName("When called then call delegate.traceRequest and reset the input stream")
        public void call_delegate() {

            ResettableHttpServletRequest resettableRequest = mock(ResettableHttpServletRequest.class);

            final byte[] requestPayload = "payload".getBytes();
            doReturn(requestPayload).when(loggingFilterSpy).getRequestPayload(resettableRequest);

            loggingFilterSpy.logRequest(resettableRequest);

            verify(loggerMock).traceRequest(
                    any(HttpRequestWrapper.class),
                    eq(requestPayload));

            verify(resettableRequest).resetInputStream();
        }
    }

    @Nested
    @DisplayName("Given logResponse()")
    public class GivenLogResponse {
        @Test
        @DisplayName("When called then call delegate.traceResponse")
        public void call_delegate() {

            ContentCachingResponseWrapper wrappedResponse = mock(ContentCachingResponseWrapper.class);
            ResettableHttpServletRequest resettableRequest = mock(ResettableHttpServletRequest.class);

            loggingFilterSpy.logResponse(wrappedResponse, resettableRequest);

            verify(loggerMock).traceResponse(
                    any(ClientHttpResponse.class),
                    any(HttpRequest.class));
        }
    }
}
