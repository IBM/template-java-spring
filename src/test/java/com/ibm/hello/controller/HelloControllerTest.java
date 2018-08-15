package com.ibm.hello.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ibm.hello.model.GreetingResponse;
import com.ibm.hello.service.HelloService;

@DisplayName("HelloController")
public class HelloControllerTest {
    HelloService serviceMock;
    HelloController controller;
    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        serviceMock = mock(HelloService.class);

        controller = new HelloController();
        ReflectionTestUtils.setField(controller, "service", serviceMock);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Nested
    @DisplayName("Given [GET] /hello")
    public class GivenGetHello {

        @Test
        @DisplayName("When called with {name} then it should return a 200 status")
        public void when_called_with_name_should_return_200_status() throws Exception {

            doReturn(new GreetingResponse()).when(serviceMock).getGreeting(anyString());

            mockMvc.perform(get("/hello?name=name"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("When called with {name} then it should return a 200 status")
        public void when_called_with_name_should_return_json_contentType() throws Exception {

            doReturn(new GreetingResponse()).when(serviceMock).getGreeting(anyString());

            mockMvc.perform(get("/hello?name=name"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        }

        @Test
        @DisplayName("When called with {name} then it should return result from service")
        public void when_called_with_name_should_call_service_createReply() throws Exception {

            final String name = "name";
            final GreetingResponse result = new GreetingResponse().withName(name).withGreeting("Result");

            doReturn(result).when(serviceMock).getGreeting(anyString());

            mockMvc.perform(get("/hello?name=" + name))
                    .andExpect(jsonPath("$.name", is(name)))
                    .andExpect(jsonPath("$.greeting", is(result.getGreeting())));

            verify(serviceMock).getGreeting(name);
        }

        @Test
        @DisplayName("When called without {name} then it should return 406 status")
        public void when_called_without_name_should_return_406() throws Exception {

            final String name = "name";

            mockMvc.perform(get("/hello"))
                    .andExpect(status().is(406));
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
    }
}
