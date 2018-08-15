package com.ibm.cloud_garage.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("HttpConnectionKeepAliveStrategy")
public class HttpConnectionKeepAliveStrategyTest {
    private HttpConnectionKeepAliveStrategy classUnderTest;

    @BeforeEach
    public void setup() {
        classUnderTest = spy(new HttpConnectionKeepAliveStrategy());
    }

    @Nested
    @DisplayName("Given getKeepAliveDuration()")
    public class GivenGetKeepAliveDuration {
        HeaderElementIterator iteratorMock;
        int defaultValue;

        public long getKeepAliveDuration() {
            HttpResponse response = mock(HttpResponse.class);
            HttpContext context = mock(HttpContext.class);

            return classUnderTest.getKeepAliveDuration(response, context);
        }

        @BeforeEach
        public void setup() {
            iteratorMock = mock(HeaderElementIterator.class);
            doReturn(iteratorMock).when(classUnderTest).buildHeaderElementIterator(any());

            defaultValue = (Integer) ReflectionTestUtils.getField(
                    HttpConnectionKeepAliveStrategy.class,
                    "MILLISECONDS_PER_MINUTE");
        }

        @Test
        @DisplayName("When timeout value provided then return timeout in milliseconds")
        public void timeout_in_milliseconds() {
            when(iteratorMock.hasNext()).thenReturn(true, false);
            HeaderElement headerElement = mock(HeaderElement.class);
            when(iteratorMock.nextElement()).thenReturn(headerElement);

            final int value = 2;
            when(headerElement.getValue()).thenReturn(String.valueOf(value));

            doReturn(true).when(classUnderTest).isNonNullTimeoutParamValue(anyString(), anyString());

            assertEquals(value * 1000, getKeepAliveDuration());
        }

        @Test
        @DisplayName("When timeout is NaN then return default timeout")
        public void timeout_NaN() {
            when(iteratorMock.hasNext()).thenReturn(true, false);
            HeaderElement headerElement = mock(HeaderElement.class);
            when(iteratorMock.nextElement()).thenReturn(headerElement);

            doReturn(true).when(classUnderTest).isNonNullTimeoutParamValue(anyString(), anyString());

            assertEquals(defaultValue, getKeepAliveDuration());
        }

        @Test
        @DisplayName("When no timeout value provided then return default timeout")
        public void no_timeout() {
            when(iteratorMock.hasNext()).thenReturn(true, false);
            HeaderElement headerElement = mock(HeaderElement.class);
            when(iteratorMock.nextElement()).thenReturn(headerElement);

            doReturn(false).when(classUnderTest).isNonNullTimeoutParamValue(anyString(), anyString());

            assertEquals(defaultValue, getKeepAliveDuration());
        }

        @Test
        @DisplayName("When no headers provided then return default timeout")
        public void no_headers() {
            when(iteratorMock.hasNext()).thenReturn(false);

            assertEquals(defaultValue, getKeepAliveDuration());
        }
    }

    @Nested
    @DisplayName("Given isNonNullTimeoutParamValue()")
    public class GivenIsNonNullTimeoutParamValue {
        @Test
        @DisplayName("When value is null then return false")
        public void null_value() {
            assertFalse(classUnderTest.isNonNullTimeoutParamValue("timeout", null));
        }

        @Test
        @DisplayName("When param is null then return false")
        public void null_param() {
            assertFalse(classUnderTest.isNonNullTimeoutParamValue(null, "1"));
        }

        @Test
        @DisplayName("When value is not null and param is 'timeout' then return true")
        public void nonNull_value_and_param_timeout() {
            assertTrue(classUnderTest.isNonNullTimeoutParamValue("timeout", "1"));
        }
    }
}
