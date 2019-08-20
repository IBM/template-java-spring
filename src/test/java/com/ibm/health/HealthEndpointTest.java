package com.ibm.health;

import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@DisplayName("HealthEndPointTest")
public class HealthEndpointTest {

    @Autowired
    private TestRestTemplate server;

    HealthEndpoint controller;

    MockMvc mockMvc;
    BeanFactory beanFactory;

    @BeforeEach
    public void setup() {

        controller = spy(new HealthEndpoint());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @org.junit.jupiter.api.Test
    @DisplayName("When called with {name} then it should return a 200 status")
    public void when_called_with_name_should_return_200_status() throws Exception {

        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString().startsWith("{\"status\":\"UP\"");
    }

}
