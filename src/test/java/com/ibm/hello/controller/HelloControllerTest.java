package com.ibm.hello.controller;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ibm.hello.config.ServiceConfig;
import com.ibm.hello.model.GreetingResponse;
import com.ibm.hello.service.GreetingService;
import com.ibm.hello.service.ServiceName;

@DisplayName("HelloController")
public class HelloControllerTest {
    HelloController controller;

    GreetingService serviceMock;
    MockMvc mockMvc;
    BeanFactory beanFactory;
    ServiceConfig serviceConfig;

    @BeforeEach
    public void setup() {
        serviceMock = mock(GreetingService.class);
        beanFactory = mock(BeanFactory.class);
        serviceConfig = new ServiceConfig();

        controller = spy(new HelloController(beanFactory, serviceConfig));

        doReturn(serviceMock).when(controller).getGreetingService(nullable(String.class));
        when(beanFactory.getBean(anyString(), eq(GreetingService.class))).thenReturn(serviceMock);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Nested
    @DisplayName("Given [GET] /hello")
    public class GivenGetHello {

        private GreetingResponse greetingResponse;

        @BeforeEach
        void setup() {

            greetingResponse = new GreetingResponse();
            doReturn(greetingResponse).when(serviceMock).getGreeting(anyString());
        }

        @Test
        @DisplayName("When called with {name} then it should return a 200 status")
        public void when_called_with_name_should_return_200_status() throws Exception {

            mockMvc.perform(get("/hello?name=name"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("When called with {name} then it should return JSON contentType")
        public void when_called_with_name_should_return_json_contentType() throws Exception {

            mockMvc.perform(get("/hello?name=name"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        }

        @Test
        @DisplayName("When called with {name} then it should return result from service")
        public void when_called_with_name_should_call_service_createReply() throws Exception {

            final String name = "name";
            greetingResponse.withName(name).withGreeting("Result");

            mockMvc.perform(get("/hello?name=" + name))
                    .andExpect(jsonPath("$.name", is(name)))
                    .andExpect(jsonPath("$.greeting", is(greetingResponse.getGreeting())));

            verify(serviceMock).getGreeting(name);
        }

        @Test
        @DisplayName("When called without {name} then it should return 406 status")
        public void when_called_without_name_should_return_406() throws Exception {

            final String name = "name";

            mockMvc.perform(get("/hello"))
                    .andExpect(status().is(406));
        }

        @Nested
        @DisplayName("When called with 'serviceName' header")
        class WhenCalledWithServiceNameHeader {

            @Test
            @DisplayName("Then it should use the header value to get the service")
            void thenItShouldUseTheHeaderValueToGetTheService() throws Exception {

                final String mockBeanName = "mock";
                mockMvc.perform(
                        get("/hello?name=name")
                                .header("serviceName", mockBeanName))
                        .andExpect(status().isOk());

                verify(controller).getGreetingService(mockBeanName);
            }
        }
    }

    @Nested
    @DisplayName("Given [POST] /hello")
    public class GivenPostHello {
        @Test
        @DisplayName("When called with empty body then it should return `400`")
        public void empty_body() throws Exception {
            mockMvc.perform(
                    post("/hello")
                            .contentType("application/json"))
                    .andExpect(status().is(400));
        }

        @Test
        @DisplayName("When called without a content type then it should return `415`")
        public void missing_content_type() throws Exception {
            mockMvc.perform(
                    post("/hello")
                            .content("{}"))
                    .andExpect(status().is(415));
        }

        @Test
        @DisplayName("When called with an empty object then it should return `406`")
        public void empty_object_in_body() throws Exception {
            mockMvc.perform(
                    post("/hello")
                            .contentType("application/json")
                            .content("{}"))
                    .andExpect(status().is(406));
        }

        @Test
        @DisplayName("When called with a name then it should return `200` and a greeting")
        public void name_in_body() throws Exception {
            final String name = "John";
            final String greeting = "greeting";

            GreetingResponse response = new GreetingResponse().withName(name).withGreeting(greeting);
            doReturn(response).when(serviceMock).getGreeting(name);

            mockMvc.perform(
                    post("/hello")
                            .contentType("application/json")
                            .content(String.format("{\"name\":\"%s\"}", name)))
                    .andExpect(status().is(200))
                    .andExpect(content().string(
                            String.format("{\"name\":\"%s\",\"greeting\":\"%s\"}", name, greeting)));
        }

        @Nested
        @DisplayName("When called with 'serviceName' header")
        class WhenCalledWithServiceNameHeader {

            @Test
            @DisplayName("Then it should use the header to get the service")
            void thenItShouldUseTheHeaderToGetTheService() throws Exception {
                final String name = "John";
                final String mockServiceName = "mock";

                doReturn(new GreetingResponse()).when(serviceMock).getGreeting(name);

                mockMvc.perform(
                        post("/hello")
                                .header("serviceName", mockServiceName)
                                .contentType("application/json")
                                .content(String.format("{\"name\":\"%s\"}", name)))
                        .andExpect(status().is(200));

                verify(controller).getGreetingService(mockServiceName);
            }
        }
    }

    @Nested
    @DisplayName("Given getGreetingService()")
    class GivenGetGreetingService {

        final String beanName = ServiceName.HOLA_NAME;

        @BeforeEach
        void setup() {
            doCallRealMethod().when(controller).getGreetingService(nullable(String.class));
            serviceConfig.setBeanName(beanName);
        }

        @Nested
        @DisplayName("When called with null header value")
        class WhenCalledWithNullHeaderValue {

            @Test
            @DisplayName("Then should return GreetingService instance from BeanFactory")
            void thenShouldReturnGreetingServiceInstanceFromBeanFactory() {

                GreetingService actualService = controller.getGreetingService(null);

                assertEquals(serviceMock, actualService);
            }

            @Test
            @DisplayName("Then should use service bean name from config")
            void thenShouldUseServiceBeanNameFromConfig() {

                controller.getGreetingService(null);

                verify(beanFactory).getBean(beanName, GreetingService.class);
            }
        }

        @Nested
        @DisplayName("When called with valid service name in header value")
        class WhenCalledWithValidServiceNameInHeaderValue {

            @Test
            @DisplayName("Then use the header value to lookup the service")
            void thenUseTheHeaderValueToLookupTheService() {

                final String validServiceName = ServiceName.HELLO_NAME;
                controller.getGreetingService(validServiceName);

                verify(beanFactory).getBean(validServiceName, GreetingService.class);
            }
        }

        @Nested
        @DisplayName("When called with invalid service name in header value")
        class WhenCalledWithInvalidServiceNameInHeaderValue {

            @Test
            @DisplayName("Then use the bean name from the config")
            void thenUseTheBeanNameFromTheConfig() {

                controller.getGreetingService("bogus");

                verify(beanFactory).getBean(beanName, GreetingService.class);
            }
        }

        @Nested
        @DisplayName("When serviceName is null")
        class WhenServiceNameIsNull {

            @BeforeEach
            void setup() {
                serviceConfig.setBeanName((ServiceName) null);
            }

            @Test
            @DisplayName("Then throw exception")
            void thenThrowException() {

                assertThrows(HelloController.ApplicationConfigurationError.class, () -> {
                    controller.getGreetingService(null);
                });
            }
        }
    }
}
