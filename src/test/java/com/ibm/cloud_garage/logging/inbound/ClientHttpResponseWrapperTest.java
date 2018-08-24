package com.ibm.cloud_garage.logging.inbound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingResponseWrapper;

@DisplayName("ClientHttpResponseWrapper")
public class ClientHttpResponseWrapperTest {
    private ContentCachingResponseWrapper responseMock;
    private ClientHttpResponseWrapper classUnderTest;
    int status;

    @BeforeEach
    public void setup() {
        status = 200;

        responseMock = mock(ContentCachingResponseWrapper.class);
        when(responseMock.getStatus()).thenReturn(status);

        classUnderTest = spy(new ClientHttpResponseWrapper(responseMock));
    }

    @Nested
    @DisplayName("Given getStatusCode()")
    public class GivenGetStatusCode {
        @Nested
        @DisplayName("When called")
        public class WhenCalled {
            @Test
            @DisplayName("Then return response.getStatus()")
            public void thenReturnResponseGetStatus() throws IOException {

                final HttpStatus actual = classUnderTest.getStatusCode();

                assertEquals(HttpStatus.OK, actual);
            }
        }
    }

    @Nested
    @DisplayName("Given getRawStatusCode()")
    public class GivenGetRawStatusCode {
        @Nested
        @DisplayName("When called")
        public class WhenCalled {
            @Test
            @DisplayName("Then return response.getStatus()")
            public void thenReturnResponseGetStatus() throws IOException {

                assertEquals(status, classUnderTest.getRawStatusCode());
            }
        }
    }

    @Nested
    @DisplayName("Given getStatusText()")
    public class GivenGetStatusText {
        @Nested
        @DisplayName("When called")
        public class WhenCalled {
            @Test
            @DisplayName("Then return getStatusCode().getReasonPhrase()")
            public void thenReturnGetStatusCodeGetReasonPhrase() throws IOException {
                final HttpStatus status = HttpStatus.OK;

                doReturn(status).when(classUnderTest).getStatusCode();

                assertEquals(status.getReasonPhrase(), classUnderTest.getStatusText());
            }
        }
    }

    @Nested
    @DisplayName("Given close()")
    public class GivenClose {
        @Nested
        @DisplayName("When called")
        public class WhenCalled {
            @Test
            @DisplayName("Then throw UnsupportedOperationException")
            public void thenThrowUnsupportedOperationException() {
                assertThrows(UnsupportedOperationException.class, () -> {
                    classUnderTest.close();
                });
            }
        }
    }

    @Nested
    @DisplayName("Given getBody()")
    public class GivenGetBody {
        @Nested
        @DisplayName("When called")
        public class WhenCalled {
            @Test
            @DisplayName("Then return ByteArrayOutputStream")
            public void thenReturnByteArrayOutputStream() throws IOException {
                final String expected = "test string";
                byte[] bytes = expected.getBytes();
                when(responseMock.getContentAsByteArray()).thenReturn(bytes);

                final InputStream inputStream = classUnderTest.getBody();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                assertEquals(expected, reader.readLine());
            }
        }
    }

    @Nested
    @DisplayName("Given getHeaders()")
    public class GivenGetHeaders {
        @Nested
        @DisplayName("When headerNames empty")
        public class WhenHeaderNamesEmpty {
            @Test
            @DisplayName("Then return empty HttpHeaders")
            public void thenReturnEmptyHttpHeaders() {
                final List<String> headerNames = new ArrayList<>();

                when(responseMock.getHeaderNames()).thenReturn(headerNames);

                assertEquals(new HttpHeaders(), classUnderTest.getHeaders());
            }
        }

        @Nested
        @DisplayName("When headerNames has values")
        public class WhenHeaderNamesHasValues {
            @Test
            @DisplayName("Then return HttpHeaders populated with headers")
            public void thenReturnHttpHeadersPopulatedWithHeaders() {
                final String headerName = "headerName";
                final String value1 = "value1";
                final String value2 = "value2";
                final List<String> headerNames = Arrays.asList(headerName);
                final List<String> headers = Arrays.asList(value1, value2);

                when(responseMock.getHeaderNames()).thenReturn(headerNames);
                when(responseMock.getHeaders(headerName)).thenReturn(headers);

                final HttpHeaders httpHeaders = classUnderTest.getHeaders();

                assertTrue(httpHeaders.containsKey(headerName));
                assertEquals(headers, httpHeaders.get(headerName));
            }
        }
    }
}

