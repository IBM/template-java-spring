package com.ibm.hello.controller;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ibm.hello.service.HelloService;

@DisplayName("HelloController")
public class HelloControllerTest {
    @Nested
    @DisplayName("Given /com.ibm.hello/{name}")
    public class GivenHello {
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

        @Test
        @DisplayName("When called with {name} then it should return a 200 status")
        public void when_called_with_name_should_return_200_status() throws Exception {

            doReturn("result").when(serviceMock).createReply(anyString());

            mockMvc.perform(get("/hello/name"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("When called with {name} then it should return result from service")
        public void when_called_with_name_should_call_service_createReply() throws Exception {

            final String name = "name";
            final String resultText = "Result";

            doReturn(resultText).when(serviceMock).createReply(anyString());

            mockMvc.perform(get("/hello/" + name))
                    .andExpect(content().string(resultText));

            verify(serviceMock).createReply(name);
        }

        @Test
        @DisplayName("When called without {name} then it should return 404 status")
        public void when_called_without_name_should_return_404() throws Exception {

            final String name = "name";

            mockMvc.perform(get("/hello/"))
                    .andExpect(status().is(404));
        }
    }
}
