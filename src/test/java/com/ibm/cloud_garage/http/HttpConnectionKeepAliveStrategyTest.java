package com.ibm.cloud_garage.http;

import static com.ibm.cloud_garage.http.HttpConnectionKeepAliveStrategy.DEFAULT_KEEP_ALIVE_DURATION;
import static com.ibm.cloud_garage.http.HttpConnectionKeepAliveStrategy.MILLISECONDS_PER_SECOND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HttpConnectionKeepAliveStrategy")
public class HttpConnectionKeepAliveStrategyTest {
    private HttpConnectionKeepAliveStrategy classUnderTest;
    private HttpResponse mockResponse;
    private HttpContext mockContext;

    @BeforeEach
    public void setup() {
        mockResponse = mock(HttpResponse.class);
        mockContext = mock(HttpContext.class);

        classUnderTest = spy(new HttpConnectionKeepAliveStrategy());
    }

    @Nested
    @DisplayName("Given getKeepAliveDuration()")
    public class GivenGetKeepAliveDuration {

        Header header;
        HeaderElement headerElement;
        long timeoutValue = 500;

        @BeforeEach
        public void setup() {
            header = mock(Header.class);
            headerElement = mock(HeaderElement.class);

            when(mockResponse.getHeaders(anyString())).thenReturn(new Header[] {header});

            when(header.getElements()).thenReturn(new HeaderElement[] {headerElement});

            when(headerElement.getName()).thenReturn("timeout");
            when(headerElement.getValue()).thenReturn(String.valueOf(timeoutValue));
        }

        @Nested
        @DisplayName("When headers are empty")
        class WhenHeadersAreEmpty {
            @Test
            @DisplayName("Then return default timeout")
            void thenReturnDefaultTimeout() {

                when(mockResponse.getHeaders(anyString())).thenReturn(new Header[] {});

                assertEquals(
                        DEFAULT_KEEP_ALIVE_DURATION,
                        classUnderTest.getKeepAliveDuration(mockResponse, mockContext));
            }
        }

        @Nested
        @DisplayName("When header contains a null element")
        class WhenHeaderContainsANullElement {
            @Test
            @DisplayName("Then return default timeout")
            void thenReturnDefaultTimeout() {

                when(mockResponse.getHeaders(anyString())).thenReturn(new Header[] {null});

                assertEquals(
                        DEFAULT_KEEP_ALIVE_DURATION,
                        classUnderTest.getKeepAliveDuration(mockResponse, mockContext));
            }
        }

        @Nested
        @DisplayName("When headerElements are empty")
        class WhenHeaderElementsAreEmpty {
            @Test
            @DisplayName("Then return default timeout")
            void thenReturnDefaultTimeout() {

                when(header.getElements()).thenReturn(new HeaderElement[] {});

                assertEquals(
                        DEFAULT_KEEP_ALIVE_DURATION,
                        classUnderTest.getKeepAliveDuration(mockResponse, mockContext));
            }
        }

        @Nested
        @DisplayName("When headerElements contains a null value")
        class WhenHeaderElementsContainsANullValue {
            @Test
            @DisplayName("Then return default timeout")
            void thenReturnDefaultTimeout() {

                when(header.getElements()).thenReturn(new HeaderElement[] {null});

                assertEquals(
                        DEFAULT_KEEP_ALIVE_DURATION,
                        classUnderTest.getKeepAliveDuration(mockResponse, mockContext)
                );
            }
        }

        @Nested
        @DisplayName("When headerElement does not contain timeout parameter")
        class WhenHeaderElementDoesNotContainTimeoutParameter {
            @Test
            @DisplayName("Then return default timeout")
            void thenReturnDefaultTimeout() {

                when(headerElement.getName()).thenReturn("not-timeout");

                assertEquals(
                        DEFAULT_KEEP_ALIVE_DURATION,
                        classUnderTest.getKeepAliveDuration(mockResponse, mockContext));
            }
        }

        @Nested
        @DisplayName("When headerElement contains a NaN timeout parameter")
        class WhenHeaderElementContainsANaNTimeoutParameter {
            @Test
            @DisplayName("Then return default timeout")
            void thenReturnDefaultTimeout() {

                when(headerElement.getValue()).thenReturn("value");

                assertEquals(
                        DEFAULT_KEEP_ALIVE_DURATION,
                        classUnderTest.getKeepAliveDuration(mockResponse, mockContext));
            }
        }

        @Nested
        @DisplayName("When headerElement contains a null value for timeout parameter")
        class WhenHeaderElementContainsANullValueForTimeoutParameter {
            @Test
            @DisplayName("Then return default timeout")
            void thenReturnDefaultTimeout() {

                when(headerElement.getValue()).thenReturn(null);

                assertEquals(
                        DEFAULT_KEEP_ALIVE_DURATION,
                        classUnderTest.getKeepAliveDuration(mockResponse, mockContext));
            }
        }

        @Nested
        @DisplayName("When headerElement contains a timeout parameter")
        class WhenHeaderElementContainsATimeoutParameter {
            @Test
            @DisplayName("Then return the timeout")
            void thenReturnTheTimeout() {

                assertEquals(
                        timeoutValue * MILLISECONDS_PER_SECOND,
                        classUnderTest.getKeepAliveDuration(mockResponse, mockContext)
                );
            }
        }
    }

    @Nested
    @DisplayName("Given getTimeoutInMilliseconds()")
    class GivenGetTimeoutInMilliseconds {
        @Nested
        @DisplayName("When timeoutValue is null")
        class WhenTimeoutValueIsNull {
            @Test
            @DisplayName("Then return null")
            void thenReturnNull() {
                assertNull(classUnderTest.getTimeoutInMilliseconds(null));
            }
        }

        @Nested
        @DisplayName("When timeoutValue is NaN")
        class WhenTimeoutValueIsNaN {
            @Test
            @DisplayName("Then return null")
            void thenReturnNull() {
                assertNull(classUnderTest.getTimeoutInMilliseconds("value"));
            }
        }

        @Nested
        @DisplayName("When timeoutValue is a number")
        class WhenTimeoutValueIsANumber {
            @Test
            @DisplayName("Then return number value * 1000")
            void thenReturnNumberValue1000() {
                int value = 500;

                assertEquals(
                        Long.valueOf(500 * 1000),
                        classUnderTest.getTimeoutInMilliseconds(String.valueOf(value))
                );
            }
        }
    }
}
