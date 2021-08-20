package com.ibm.cloud_native_toolkit.rest_template;

import static com.ibm.cloud_native_toolkit.rest_template.RestTemplateBuilder.MILLISECONDS_PER_SECOND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.ibm.cloud_native_toolkit.logging.outbound.LoggingInterceptor;

@DisplayName("RestTemplateBuilder")
public class RestTemplateBuilderTest {
    RestTemplateBuilder classUnderTest;
    RestTemplateProperties restTemplateProperties;

    @BeforeEach
    public void setup() {
        classUnderTest = spy(new RestTemplateBuilder());

        restTemplateProperties = new SimpleRestTemplateProperties();
    }

    @Nested
    @DisplayName("Given build()")
    class GivenBuild {
        ClientHttpRequestFactory clientHttpRequestFactory;
        List<ClientHttpRequestInterceptor> interceptors;

        @BeforeEach
        void setup() {

            clientHttpRequestFactory = mock(ClientHttpRequestFactory.class);
            interceptors = new ArrayList<>();

            doReturn(clientHttpRequestFactory).when(classUnderTest).buildClientHttpRequestFactory(any());
            doReturn(interceptors).when(classUnderTest).buildInterceptors(any());
        }


        @Nested
        @DisplayName("When restTemplateProperties is null")
        class WhenRestTemplatePropertiesIsNull {
            @Test
            @DisplayName("Then return empty RestTemplate")
            void thenReturnEmptyRestTemplate() {

                final RestTemplate actual = classUnderTest.build(null);

                assertNotEquals(clientHttpRequestFactory, actual.getRequestFactory());
                assertEquals(new ArrayList<>(), actual.getInterceptors());
            }
        }

        @Nested
        @DisplayName("When restTemplateProperties is not null")
        class WhenRestTemplatePropertiesIsNotNull {
            @Test
            @DisplayName("Then it should set requestFactory and interceptors")
            void thenItShouldSetRequestFactoryAndInterceptors() {

                final RestTemplateProperties properties = mock(RestTemplateProperties.class);

                final RestTemplate actual = classUnderTest.build(properties);

            }
        }
    }

    @Nested
    @DisplayName("Given buildClientHttpRequestFactory()")
    class GivenBuildClientHttpRequestFactory {
        @Nested
        @DisplayName("When called")
        class WhenCalled {
            @Test
            @DisplayName("Then return non-null ClientHttpRequestFactory")
            void thenReturnNonNullClientHttpRequestFactory() {
                HttpClient httpClient = mock(HttpClient.class);

                doReturn(httpClient).when(classUnderTest).buildHttpClient(restTemplateProperties);

                assertNotNull(classUnderTest.buildClientHttpRequestFactory(restTemplateProperties));
                verify(classUnderTest).buildHttpClient(restTemplateProperties);
            }
        }
    }

    @Nested
    @DisplayName("Given buildHttpClient()")
    class GivenBuildHttpClient {
        @Nested
        @DisplayName("When called")
        class WhenCalled {
            @Test
            @DisplayName("Then return HttpClient populated from restTemplateProperties")
            void thenReturnHttpClientPopulatedFromRestTemplateProperties() {
                final RestTemplateProperties props = mock(RestTemplateProperties.class);

                final ConnectionKeepAliveStrategy connectionKeepAliveStrategy = mock(ConnectionKeepAliveStrategy.class);
                final HttpClientConnectionManager connectionManager = mock(HttpClientConnectionManager.class);
                final RequestConfig requestConfig = mock(RequestConfig.class);
                final HttpHost httpHost = new HttpHost("x", 80);

                doReturn(connectionKeepAliveStrategy).when(classUnderTest).buildConnectionKeepAliveStrategy();
                doReturn(connectionManager).when(classUnderTest).buildConnectionManager(props);
                doReturn(requestConfig).when(classUnderTest).buildRequestConfig(props);
                doReturn(httpHost).when(classUnderTest).buildProxySettings(props);

                final HttpClient actual = classUnderTest.buildHttpClient(props);

                verify(classUnderTest).buildConnectionKeepAliveStrategy();
                verify(classUnderTest).buildConnectionManager(props);
                verify(classUnderTest).buildRequestConfig(props);
                verify(classUnderTest).buildProxySettings(props);
            }
        }
    }

    @Nested
    @DisplayName("Given buildConnectionKeelAliveStrategy()")
    class GivenBuildConnectionKeelAliveStrategy {
        @Nested
        @DisplayName("When called")
        class WhenCalled {
            @Test
            @DisplayName("Then return non-null instance")
            void thenReturnNonNullInstance() {
                assertNotNull(classUnderTest.buildConnectionKeepAliveStrategy());
            }
        }
    }

    @Nested
    @DisplayName("Given buildConnectionManager()")
    class GivenBuildConnectionManager {
        @Nested
        @DisplayName("When called")
        class WhenCalled {
            @Test
            @DisplayName("Then return connectionManager with maxTotal connections")
            void thenReturnConnectionManagerWithMaxTotalConnections() {
                final RestTemplateProperties props = mock(RestTemplateProperties.class);

                final int connections = 3;
                when(props.getMaxTotalConnections()).thenReturn(connections);

                final PoolingHttpClientConnectionManager actual =
                        (PoolingHttpClientConnectionManager) classUnderTest.buildConnectionManager(props);

                assertEquals(connections, actual.getMaxTotal());
                assertEquals(connections, actual.getDefaultMaxPerRoute());
            }
        }
    }

    @Nested
    @DisplayName("Given buildRequestConfig()")
    class GivenBuildRequestConfig {
        @Nested
        @DisplayName("When called")
        class WhenCalled {
            @Test
            @DisplayName("Then return requestConfig")
            void thenReturnRequestConfig() {
                final RestTemplateProperties props = mock(RestTemplateProperties.class);

                final int socketTimeout = 1;
                final int connectTimeout = 2;
                final int connectionRequestTimeout = 3;

                when(props.getSocketTimeOutInSeconds()).thenReturn(socketTimeout);
                when(props.getConnectTimeOutInSeconds()).thenReturn(connectTimeout);
                when(props.getConnectionRequestTimeOutInSeconds()).thenReturn(connectionRequestTimeout);

                final RequestConfig actual = classUnderTest.buildRequestConfig(props);

                assertEquals(socketTimeout * MILLISECONDS_PER_SECOND, actual.getSocketTimeout());
                assertEquals(connectTimeout * MILLISECONDS_PER_SECOND, actual.getConnectTimeout());
                assertEquals(connectionRequestTimeout * MILLISECONDS_PER_SECOND, actual.getConnectionRequestTimeout());
            }
        }
    }

    @Nested
    @DisplayName("Given buildProxySettings()")
    class GivenBuildProxySettings {
        @Nested
        @DisplayName("When proxy is not required")
        class WhenProxyIsNotRequired {
            @Test
            @DisplayName("Then return null")
            void thenReturnNull() {
                final RestTemplateProperties props = mock(RestTemplateProperties.class);

                when(props.isProxyRequired()).thenReturn(false);

                assertNull(classUnderTest.buildProxySettings(props));
            }
        }

        @Nested
        @DisplayName("When proxy is required")
        class WhenProxyIsRequired {
            @Test
            @DisplayName("Then return HttpHost with hostname and port")
            void thenReturnHttpHostWithHostnameAndPort() {
                final RestTemplateProperties props = mock(RestTemplateProperties.class);

                final String hostname = "hostname";
                int port = 80;
                when(props.getProxyHostname()).thenReturn(hostname);
                when(props.getProxyPort()).thenReturn(port);
                when(props.isProxyRequired()).thenReturn(true);

                HttpHost proxySettings = classUnderTest.buildProxySettings(props);

                assertEquals(hostname, proxySettings.getHostName());
                assertEquals(port, proxySettings.getPort());
            }
        }
    }

    @Nested
    @DisplayName("Given buildInterceptors()")
    class GivenBuildInterceptors {
        @Nested
        @DisplayName("When loggingInterceptor is null")
        class WhenLoggingInterceptorIsNull {
            @Test
            @DisplayName("Then return default LoggingInterceptor")
            void thenReturnDefaultLoggingInterceptor() {
                List<ClientHttpRequestInterceptor> interceptorList = classUnderTest.buildInterceptors(null);

                assertFalse(interceptorList.isEmpty());
                assertTrue(interceptorList.get(0) instanceof LoggingInterceptor);
            }
        }

        @Nested
        @DisplayName("When loggingInterceptor is not null")
        class WhenLoggingInterceptorIsNotNull {
            @Test
            @DisplayName("Then return loggingInterceptor")
            void thenReturnLoggingInterceptor() {
                ClientHttpRequestInterceptor interceptor = mock(ClientHttpRequestInterceptor.class);

                List<ClientHttpRequestInterceptor> interceptorList = classUnderTest.buildInterceptors(interceptor);

                assertEquals(Arrays.asList(interceptor), interceptorList);
            }
        }
    }

    @Nested
    @DisplayName("Given close()")
    class GivenClose {
        RestTemplate restTemplate;

        @BeforeEach
        void setup() {
            restTemplate = mock(RestTemplate.class);
        }

        @Nested
        @DisplayName("When restTemplate is null")
        class WhenRestTemplateIsNull {
            @Test
            @DisplayName("Then do nothing")
            void thenDoNothing() {
                classUnderTest.close(null);
            }
        }

        @Nested
        @DisplayName("When restTemplate request factory not an instance of DisposableBean")
        class WhenRestTemplateRequestFactoryNotAnInstanceOfDisposableBean {
            @Test
            @DisplayName("Then do nothing")
            void thenDoNothing() {
                when(restTemplate.getRequestFactory()).thenReturn(null);

                classUnderTest.close(restTemplate);

                verify(restTemplate).getRequestFactory();
            }
        }

        @Nested
        @DisplayName("When restTemplate request factory is an instance of DisposableBean")
        class WhenRestTemplateRequestFactoryIsAnInstanceOfDisposableBean {

            @Test
            @DisplayName("Then destroy the requestFactory")
            void thenDestroyTheRequestFactory() throws Exception {
                DisposableClientHttpRequestFactory disposableRequestFactory =
                        mock(DisposableClientHttpRequestFactory.class);
                when(restTemplate.getRequestFactory()).thenReturn(disposableRequestFactory);

                classUnderTest.close(restTemplate);

                verify(disposableRequestFactory).destroy();
            }

            @Test
            @DisplayName("And ignore exceptions during destroy")
            void andIgnoreExceptionsDuringDestroy() throws Exception {
                DisposableClientHttpRequestFactory disposableRequestFactory =
                        mock(DisposableClientHttpRequestFactory.class);
                when(restTemplate.getRequestFactory()).thenReturn(disposableRequestFactory);

                doThrow(Exception.class).when(disposableRequestFactory).destroy();

                classUnderTest.close(restTemplate);

                verify(disposableRequestFactory).destroy();
            }
        }
    }

    interface DisposableClientHttpRequestFactory extends ClientHttpRequestFactory, DisposableBean {
    }

}

