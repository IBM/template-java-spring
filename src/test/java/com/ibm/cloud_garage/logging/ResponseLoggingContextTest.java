package com.ibm.cloud_garage.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@DisplayName("ResponseLoggingContext")
public class ResponseLoggingContextTest {
    String url = "url";
    String statusCode = "status";
    String statusText = "text";
    HttpHeaders headers = new HttpHeaders();
    Object body = "body";

    private ResponseLoggingContext classUnderTest;

    ResponseLoggingContext copyClassUnderTest() {
        return new ResponseLoggingContext(classUnderTest);
    }

    @BeforeEach
    public void setup() {
        headers.add("key", "value");

        classUnderTest = new ResponseLoggingContext()
                .withUrl(url)
                .withStatusCode(statusCode)
                .withStatusText(statusText)
                .withHeaders(headers)
                .withBody(body);
    }

    @Nested
    @DisplayName("Given constructor")
    class GivenConstructor {
        @Nested
        @DisplayName("When `context` is null")
        class WhenContextIsNull {
            @Test
            @DisplayName("Then throw exception")
            void thenThrowException() {
                assertThrows(IllegalArgumentException.class, () -> {
                    new ResponseLoggingContext(null);
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
                assertEquals(statusCode, classUnderTest.getStatusCode());
                assertEquals(statusText, classUnderTest.getStatusText());
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
        @DisplayName("When value is the same instance")
        class WhenValueIsTheSameInstance {
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
        @DisplayName("When value is the same type")
        class WhenValueIsTheSameType {
            @Test
            @DisplayName("Then same values should return true")
            void thenSameValuesShouldReturnTrue() {
                assertEquals(classUnderTest, copyClassUnderTest());
            }

            @Test
            @DisplayName("Then different `url` should return false")
            void thenDifferentUrlShouldReturnFalse() {
                assertNotEquals(classUnderTest, copyClassUnderTest().withUrl(null));
            }

            @Test
            @DisplayName("Then different `statusCode` should return false")
            void thenDifferentStatusCodeShouldReturnFalse() {
                assertNotEquals(classUnderTest, copyClassUnderTest().withStatusCode(null));
            }

            @Test
            @DisplayName("Then different statusText should return false")
            void thenDifferentStatusTextShouldReturnFalse() {
                assertNotEquals(classUnderTest, copyClassUnderTest().withStatusText(null));
            }

            @Test
            @DisplayName("Then different headers should return false")
            void thenDifferentHeadersShouldReturnFalse() {
                assertNotEquals(classUnderTest, copyClassUnderTest().withHeaders(null));
            }

            @Test
            @DisplayName("Then different body should return false")
            void thenDifferentBodyShouldReturnFalse() {
                assertNotEquals(classUnderTest, copyClassUnderTest().withBody(null));
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
        @DisplayName("When same values are provided")
        class WhenSameValuesAreProvided {
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
        @DisplayName("When different `statusCode` provided")
        class WhenDifferentStatusCodeProvided {
            @Test
            @DisplayName("Then return different hashCode")
            void thenReturnDifferentHashCode() {
                assertHashCodesNotEqual(copyClassUnderTest().withStatusCode(null));
            }
        }

        @Nested
        @DisplayName("When different `statusText` provided")
        class WhenDifferentStatusTextProvided {
            @Test
            @DisplayName("Then return different hashCode")
            void thenReturnDifferentHashCode() {
                assertHashCodesNotEqual(copyClassUnderTest().withStatusText(null));
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

