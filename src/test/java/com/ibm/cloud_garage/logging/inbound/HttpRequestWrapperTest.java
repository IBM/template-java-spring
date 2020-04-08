package com.ibm.cloud_garage.logging.inbound;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@DisplayName("HttpRequestWrapper")
public class HttpRequestWrapperTest {
    private HttpRequestWrapper classUnderTest;
    private HttpServletRequest requestMock;

    @BeforeEach
    public void setup() {
        requestMock = mock(HttpServletRequest.class);
        HttpRequestWrapper original = new HttpRequestWrapper(requestMock);

        classUnderTest = spy(original);
    }

    @Nested
    @DisplayName("Given getMethodValue()")
    public class GivenGetMethodValue {
        @Nested
        @DisplayName("When called")
        public class WhenCalled {
            @Test
            @DisplayName("Then return request.getMethod()")
            public void thenReturnRequestGetMethod() {
                final String expected = "method";
                when(requestMock.getMethod()).thenReturn(expected);

                final String actual = classUnderTest.getMethodValue();

                assertEquals(expected, actual);
            }
        }
    }

    @Nested
    @DisplayName("Given getURI()")
    public class GivenGetUri {
        @Nested
        @DisplayName("When called successfully")
        public class WhenCalledSuccessfully {
            @Test
            @DisplayName("Then return URI")
            public void thenReturnUri() throws URISyntaxException {
                final String requestPath = "/request";
                final String requestUri = "/requestURI";
                final Map<String, String[]> parameterMap = new HashMap<>();

                when(requestMock.getRequestURI()).thenReturn(requestUri);
                when(requestMock.getParameterMap()).thenReturn(parameterMap);

                doReturn(requestPath).when(classUnderTest).getRequestPath(requestUri, parameterMap);

                final URI actual = classUnderTest.getURI();

                assertEquals(new URI(requestPath), actual);
            }
        }

        @Nested
        @DisplayName("When exception thrown")
        public class WhenExceptionThrown {
            @Test
            @DisplayName("Then return null")
            public void thenReturnNull() {
                when(requestMock.getRequestURI()).thenReturn("httpx;\\bad/url");

                assertNull(classUnderTest.getURI());
            }
        }
    }

    @Nested
    @DisplayName("Given getRequestPath()")
    public class GivenGetRequestPath {
        @Test
        @DisplayName("When requestUri is null then return '/'")
        public void null_request_uri_return_forward_slash() {
            assertEquals("/", classUnderTest.getRequestPath(null, new HashMap<>()));
        }

        @Test
        @DisplayName("When requestUri is '/test' and parameterMap is null then return '/test'")
        public void null_parameterMap_returns_requestUri() {
            final String requestUri = "/test";

            assertEquals(requestUri, classUnderTest.getRequestPath(requestUri, null));
        }

        @Test
        @DisplayName("When requestUri is '/test' and parameterMap is 'key'=['value'] then return '/test?key=value'")
        public void single_value_param() {
            final String requestUri = "/test";
            final String paramName = "key";
            final String paramValue = "value";

            Map<String, String[]> parameterMap = new HashMap<>();
            parameterMap.put(paramName, new String[] {paramValue});

            assertEquals(
                    requestUri + "?" + paramName + "=" + paramValue,
                    classUnderTest.getRequestPath(requestUri, parameterMap));
        }

        @Test
        @DisplayName("When requestUri is '/test' and parameterMap is 'key'=['value1','value2'] "
                + "then return '/test?key=value1&key=value2'")
        public void multiple_value_param() {
            final String requestUri = "/test";
            final String paramName = "key";
            final String paramValue1 = "value1";
            final String paramValue2 = "value2";

            Map<String, String[]> parameterMap = new HashMap<>();
            parameterMap.put(paramName, new String[] {paramValue1, paramValue2});

            assertEquals(
                    requestUri + "?" + paramName + "=" + paramValue1 + "&" + paramName + "=" + paramValue2,
                    classUnderTest.getRequestPath(requestUri, parameterMap));
        }

        @Test
        @DisplayName("When requestUri is '/test' and parameterMap is 'key1'=['value1'],'key2'=['value2'] "
                + "then return '/test?key1=value1&key2=value2'")
        public void multiple_params() {
            final String requestUri = "/test";
            final String paramName1 = "key1";
            final String paramName2 = "key2";
            final String paramValue1 = "value1";
            final String paramValue2 = "value2";

            Map<String, String[]> parameterMap = new HashMap<>();
            parameterMap.put(paramName1, new String[] {paramValue1});
            parameterMap.put(paramName2, new String[] {paramValue2});

            assertEquals(
                    requestUri + "?" + paramName1 + "=" + paramValue1 + "&" + paramName2 + "=" + paramValue2,
                    classUnderTest.getRequestPath(requestUri, parameterMap));
        }
    }

    @Nested
    @DisplayName("Given getHeaders()")
    public class GivenGetHeaders {
        @Nested
        @DisplayName("When no headerNames")
        public class WhenNoHeaderNames {
            @Test
            @DisplayName("Then return empty HttpHeaders")
            public void thenReturnEmptyHttpHeaders() {
                final Enumeration headerNames = mock(Enumeration.class);
                when(headerNames.hasMoreElements()).thenReturn(false);

                when(requestMock.getHeaderNames()).thenReturn(headerNames);

                final HttpHeaders actual = classUnderTest.getHeaders();

                assertEquals(new HttpHeaders(), actual);
            }
        }

        @Nested
        @DisplayName("When one header name")
        public class WhenOneHeaderName {
            @Test
            @DisplayName("Then return HttpHeaders with single value")
            public void thenReturnHttpHeadersWithSingleValue() {

                final String headerName = "header";
                final Vector headerNamesVector = new Vector<String>();
                headerNamesVector.add(headerName);
                final Enumeration headerNames = headerNamesVector.elements();

                final String value1 = "value1";
                final String value2 = "value2";
                final Vector headersVector = new Vector<String>();
                headersVector.add(value1);
                headersVector.add(value2);
                final Enumeration headers = headersVector.elements();

                when(requestMock.getHeaderNames()).thenReturn(headerNames);
                when(requestMock.getHeaders(headerName)).thenReturn(headers);

                final HttpHeaders actual = classUnderTest.getHeaders();

                assertTrue(actual.containsKey(headerName));
                assertEquals(Arrays.asList(value1, value2), actual.get(headerName));
            }
        }
    }

    @Nested
    @DisplayName("Given EnumerationIterator")
    public class GivenEnumerationIterator {

        @Nested
        @DisplayName("When TrytoReadPassedTheEndofIterator")
        public class WhenTrytoReadPassedTheEndofIterator {

            @Test
            @DisplayName("Then throw exception")
            public void thenThrowException() {

                Enumeration enumVar = new Vector<String>().elements();
                HttpRequestWrapper.EnumerationIterator classUnderTest =
                        new HttpRequestWrapper.EnumerationIterator(enumVar);

                assertThrows(NoSuchElementException.class, () -> {
                    classUnderTest.next();
                });

            }
        }

    }


}
