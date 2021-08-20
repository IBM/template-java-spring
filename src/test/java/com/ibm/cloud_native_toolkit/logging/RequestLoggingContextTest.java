package com.ibm.cloud_native_toolkit.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@DisplayName("RequestLoggingContext")
public class RequestLoggingContextTest {
    String url = "url";
    String method = "method";
    HttpHeaders headers = new HttpHeaders();
    Object body = "body";
    RequestLoggingContext classUnderTest;

    RequestLoggingContext copyClassUnderTest() {
        return new RequestLoggingContext(classUnderTest);
    }

    @BeforeEach
    public void setup() {
        headers.add("test", "value");

        classUnderTest = new RequestLoggingContext()
                .withUrl(url)
                .withMethod(method)
                .withHeaders(headers)
                .withBody(body);
    }

    @Nested
    @DisplayName("Given constructor()")
    class GivenConstructor {
        @Nested
        @DisplayName("When `context` is null")
        class WhenContextIsNull {
            @Test
            @DisplayName("Then throw IllegalArgumentException")
            void thenThrowIllegalArgumentException() {
                assertThrows(IllegalArgumentException.class, () -> {
                    new RequestLoggingContext(null);
                });
            }
        }
    }

    @Nested
    @DisplayName("Given getters and setters()")
    class GivenGettersAndSetters {
        @Nested
        @DisplayName("When called")
        class WhenCalled {
            @Test
            @DisplayName("Then return values")
            void thenReturnValues() {
                assertEquals(url, classUnderTest.getUrl());
                assertEquals(method, classUnderTest.getMethod());
                assertEquals(headers, classUnderTest.getHeaders());
                assertEquals(body, classUnderTest.getBody());
            }
        }
    }

    @Nested
    @DisplayName("Given equals()")
    class GivenEquals {
        @Nested
        @DisplayName("When value is null")
        class WhenValueIsNull {
            @Test
            @DisplayName("Then return false")
            void thenReturnFalse() {
                assertFalse(classUnderTest.equals(null));
            }
        }

        @Nested
        @DisplayName("When value is same instance")
        class WhenValueIsSameInstance {
            @Test
            @DisplayName("Then return true")
            void thenReturnTrue() {
                assertTrue(classUnderTest.equals(classUnderTest));
            }
        }

        @Nested
        @DisplayName("When value is a different type")
        class WhenValueIsADifferentType {
            @Test
            @DisplayName("Then return false")
            void thenReturnFalse() {
                assertFalse(classUnderTest.equals("test"));
            }
        }

        @Nested
        @DisplayName("When value is same type")
        class WhenValueIsSameType {
            @Test
            @DisplayName("Then same values should return true")
            void thenSameValuesShouldReturnTrue() {
                assertTrue(classUnderTest.equals(copyClassUnderTest()));
            }

            @Test
            @DisplayName("Then different `url` should return false")
            void thenDifferentUrlShouldReturnFalse() {
                assertFalse(classUnderTest.equals(copyClassUnderTest().withUrl(null)));
            }

            @Test
            @DisplayName("Then different `method` should return false")
            void thenDifferentMethodShouldReturnFalse() {
                assertFalse(classUnderTest.equals(copyClassUnderTest().withMethod(null)));
            }

            @Test
            @DisplayName("Then different `headers` should return false")
            void thenDifferentHeadersShouldReturnFalse() {
                assertFalse(classUnderTest.equals(copyClassUnderTest().withHeaders(null)));
            }

            @Test
            @DisplayName("Then different `body` should return false")
            void thenDifferentBodyShouldReturnFalse() {
                assertFalse(classUnderTest.equals(copyClassUnderTest().withBody(null)));
            }
        }
    }

    @Nested
    @DisplayName("Given hashCode()")
    class GivenHashCode {
        void assertHashCodesEqual(Object actual) {
            assertTrue(hashCodesEqual(classUnderTest, actual));
        }

        void assertHashCodesNotEqual(Object actual) {
            assertFalse(hashCodesEqual(classUnderTest, actual));
        }

        boolean hashCodesEqual(Object o1, Object o2) {
            return o1.hashCode() == o2.hashCode();
        }

        @Nested
        @DisplayName("When same values provided")
        class WhenSameValuesProvided {
            @Test
            @DisplayName("Then return same hashCode")
            void thenReturnSameHashCode() {
                assertHashCodesEqual(copyClassUnderTest());
            }
        }

        @Nested
        @DisplayName("When different `url` provided")
        class WhenDifferentUrlProvided {
            @Test
            @DisplayName("Then return different hashCode")
            void thenReturnDifferentHashCode() {
                assertHashCodesNotEqual(copyClassUnderTest().withUrl(null));
            }
        }

        @Nested
        @DisplayName("When different `method` provided")
        class WhenDifferentMethodProvided {
            @Test
            @DisplayName("Then return different hashCode")
            void thenReturnDifferentHashCode() {
                assertHashCodesNotEqual(copyClassUnderTest().withMethod(null));
            }
        }

        @Nested
        @DisplayName("When different `headers` provided")
        class WhenDifferentHeadersProvided {
            @Test
            @DisplayName("Then return different hashCode")
            void thenReturnDifferentHashCode() {
                assertHashCodesNotEqual(copyClassUnderTest().withHeaders(null));
            }
        }

        @Nested
        @DisplayName("When different `body` provided")
        class WhenDifferentBodyProvided {
            @Test
            @DisplayName("Then return different hashCode")
            void thenReturnDifferentHashCode() {
                assertHashCodesNotEqual(copyClassUnderTest().withBody(null));
            }
        }
    }
}
