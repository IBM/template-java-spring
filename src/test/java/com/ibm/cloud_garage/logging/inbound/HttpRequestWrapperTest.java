package com.ibm.cloud_garage.logging.inbound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HttpRequestWrapper")
public class HttpRequestWrapperTest {
    private HttpRequestWrapper httpRequestWrapperSpy;
    private HttpServletRequest requestMock;

    @BeforeEach
    public void setup() {
        requestMock = mock(HttpServletRequest.class);
        HttpRequestWrapper original = new HttpRequestWrapper(requestMock);

        httpRequestWrapperSpy = spy(original);
    }

    @Nested
    @DisplayName("Given getRequestPath()")
    public class GivenGetRequestPath {
        @Test
        @DisplayName("When requestUri is null then return '/'")
        public void null_request_uri_return_forward_slash() {
            assertEquals("/", httpRequestWrapperSpy.getRequestPath(null, new HashMap<>()));
        }

        @Test
        @DisplayName("When requestUri is '/test' and parameterMap is null then return '/test'")
        public void null_parameterMap_returns_requestUri() {
            final String requestUri = "/test";

            assertEquals(requestUri, httpRequestWrapperSpy.getRequestPath(requestUri, null));
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
                    httpRequestWrapperSpy.getRequestPath(requestUri, parameterMap));
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
                    httpRequestWrapperSpy.getRequestPath(requestUri, parameterMap));
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
                    httpRequestWrapperSpy.getRequestPath(requestUri, parameterMap));
        }
    }
}
