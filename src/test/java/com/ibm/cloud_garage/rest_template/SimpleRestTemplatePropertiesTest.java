package com.ibm.cloud_garage.rest_template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.ClientHttpRequestInterceptor;

@DisplayName("SimpleRestTemplateProperties")
class SimpleRestTemplatePropertiesTest {
    SimpleRestTemplateProperties classUnderTest;
    private int connectRequestTimeout = 1;
    private int connectTimeout = 2;
    private int maxTotalConnections = 3;
    private int socketTimeout = 4;
    private String providerEndpoint = "endpoint";
    private String proxyHostname = "hostname";
    private int proxyPort = 80;
    private ClientHttpRequestInterceptor loggingInterceptor;

    @BeforeEach
    void setup() {
        loggingInterceptor = mock(ClientHttpRequestInterceptor.class);

        classUnderTest = new SimpleRestTemplateProperties()
                .withConnectionRequestTimeOutInSeconds(connectRequestTimeout)
                .withConnectTimeOutInSeconds(connectTimeout)
                .withSocketTimeOutInSeconds(socketTimeout)
                .withMaxTotalConnections(maxTotalConnections)
                .withProviderEndpoint(providerEndpoint)
                .withProxyHostname(proxyHostname)
                .withProxyPort(proxyPort)
                .withLoggingInterceptor(loggingInterceptor);
    }

    @Nested
    @DisplayName("Given getters and setters")
    class GivenGettersAndSetters {
        @Nested
        @DisplayName("When called")
        class WhenCalled {
            @Test
            @DisplayName("Then return values")
            void thenReturnValues() {
                assertEquals(connectRequestTimeout, classUnderTest.getConnectionRequestTimeOutInSeconds());
                assertEquals(connectTimeout, classUnderTest.getConnectTimeOutInSeconds());
                assertEquals(socketTimeout, classUnderTest.getSocketTimeOutInSeconds());
                assertEquals(maxTotalConnections, classUnderTest.getMaxTotalConnections());
                assertEquals(providerEndpoint, classUnderTest.getProviderEndpoint());
                assertEquals(proxyHostname, classUnderTest.getProxyHostname());
                assertEquals(proxyPort, classUnderTest.getProxyPort());
                assertEquals(loggingInterceptor, classUnderTest.getLoggingInterceptor());
            }
        }
    }

    @Nested
    @DisplayName("Given isProxyRequired()")
    class GivenIsProxyRequired {
        @Nested
        @DisplayName("When proxyHostname is null")
        class WhenProxyHostnameIsNull {
            @Test
            @DisplayName("Then return false")
            void thenReturnFalse() {
                classUnderTest.setProxyPort(80);
                classUnderTest.setProxyHostname(null);

                assertFalse(classUnderTest.isProxyRequired());
            }
        }

        @Nested
        @DisplayName("When proxyPort is less than or equal to 0")
        class WhenProxyPortIsLessThanOrEqualTo0 {
            @Test
            @DisplayName("Then return false")
            void thenReturnFalse() {
                classUnderTest.setProxyHostname("test");
                classUnderTest.setProxyPort(-1);

                assertFalse(classUnderTest.isProxyRequired());
            }
        }

        @Nested
        @DisplayName("When proxyHostname is not null and proxyPort is greater than 0")
        class WhenProxyHostnameIsNotNullAndProxyPortIsGreaterThan0 {
            @Test
            @DisplayName("Then return true")
            void thenReturnTrue() {
                classUnderTest.setProxyHostname("test");
                classUnderTest.setProxyPort(80);

                assertTrue(classUnderTest.isProxyRequired());
            }
        }
    }

    @Nested
    @DisplayName("Given equals()")
    class GivenEquals {
        @Nested
        @DisplayName("When input is the same instance")
        class WhenInputIsTheSameInstance {
            @Test
            @DisplayName("Then return true")
            void thenReturnTrue() {
                assertTrue(classUnderTest.equals(classUnderTest));
            }
        }

        @Nested
        @DisplayName("When input is a different type")
        class WhenInputIsADifferentType {
            @Test
            @DisplayName("Then return false")
            void thenReturnFalse() {
                assertFalse(classUnderTest.equals("test"));
            }
        }

        @Nested
        @DisplayName("When input is the same type")
        class WhenInputIsTheSameType {
            @Test
            @DisplayName("Then compare all fields")
            void thenCompareAllFields() {
                assertEquals(classUnderTest, new SimpleRestTemplateProperties(classUnderTest));
                assertNotEquals(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withProviderEndpoint(null));
                assertNotEquals(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withMaxTotalConnections(10));
                assertNotEquals(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withConnectTimeOutInSeconds(10));
                assertNotEquals(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withSocketTimeOutInSeconds(10));
                assertNotEquals(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withConnectionRequestTimeOutInSeconds(10));
                assertNotEquals(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withProxyHostname(null));
                assertNotEquals(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withProxyPort(10));
                assertNotEquals(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withLoggingInterceptor(null));
            }
        }
    }

    @Nested
    @DisplayName("Given hashCode()")
    class GivenHashCode {
        void assertHashCodesEqual(Object expected, Object actual) {
            assertEquals(expected.hashCode(), actual.hashCode());
        }

        void assertHashCodesNotEqual(Object expected, Object actual) {
            assertNotEquals(expected.hashCode(), actual.hashCode());
        }

        @Nested
        @DisplayName("When same field values provided")
        class WhenSameFieldValuesProvided {
            @Test
            @DisplayName("Then return same hashCode")
            void thenReturnSameHashCode() {
                assertHashCodesEqual(classUnderTest, new SimpleRestTemplateProperties(classUnderTest));
            }
        }

        @Nested
        @DisplayName("When different field values provided")
        class WhenDifferentFieldValuesProvided {
            @Test
            @DisplayName("Then return different hashCode")
            void thenReturnDifferentHashCode() {
                assertHashCodesNotEqual(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withProviderEndpoint(null));
                assertHashCodesNotEqual(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withMaxTotalConnections(10));
                assertHashCodesNotEqual(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withConnectTimeOutInSeconds(10));
                assertHashCodesNotEqual(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withSocketTimeOutInSeconds(10));
                assertHashCodesNotEqual(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withConnectionRequestTimeOutInSeconds(10));
                assertHashCodesNotEqual(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withProxyHostname(null));
                assertHashCodesNotEqual(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withProxyPort(10));
                assertHashCodesNotEqual(classUnderTest, new SimpleRestTemplateProperties(classUnderTest)
                        .withLoggingInterceptor(null));
            }
        }
    }

}