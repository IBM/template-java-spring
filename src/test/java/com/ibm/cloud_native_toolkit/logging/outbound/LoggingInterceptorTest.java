package com.ibm.cloud_native_toolkit.logging.outbound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;

import com.ibm.cloud_native_toolkit.logging.RequestResponseLogger;

@DisplayName("LoggingInterceptor")
public class LoggingInterceptorTest {
    LoggingInterceptor classUnderTest;
    RequestResponseLogger delegateMock;

    @BeforeEach
    public void setup() {
        classUnderTest = new LoggingInterceptor();

        delegateMock = mock(RequestResponseLogger.class);
        ReflectionTestUtils.setField(classUnderTest, "delegate", delegateMock);
    }

    @Nested
    @DisplayName("Given intercept()")
    public class GivenIntercept {
        @Test
        @DisplayName("When called then it should traceRequest, execute next, then traceResponse")
        public void execute() throws IOException {
            HttpRequest request = mock(HttpRequest.class);
            byte[] body = new byte[] {};
            ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
            ClientHttpResponse response = mock(ClientHttpResponse.class);

            doReturn(response).when(execution).execute(request, body);

            ClientHttpResponse actualResponse = classUnderTest.intercept(request, body, execution);

            assertEquals(response, actualResponse);

            verify(delegateMock).traceRequest(request, body);
            verify(execution).execute(request, body);
            verify(delegateMock).traceResponse(response, request);
        }

        @Test
        @DisplayName("When execute throws exception then it should still call traceResponse")
        public void exception() throws IOException {
            HttpRequest request = mock(HttpRequest.class);
            byte[] body = new byte[] {};
            ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
            ClientHttpResponse response = mock(ClientHttpResponse.class);

            doThrow(IOException.class).when(execution).execute(request, body);

            assertThrows(IOException.class, () -> {
                classUnderTest.intercept(request, body, execution);
            });

            verify(delegateMock).traceRequest(request, body);
            verify(execution).execute(request, body);
            verify(delegateMock).traceResponse(null, request);
        }
    }
}
