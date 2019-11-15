package com.ibm.inventory_management.controllers;

import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@DisplayName("StockItemController")
public class StockItemControllerTest {
    StockItemController controller;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        controller = spy(new StockItemController());

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Nested
    @DisplayName("Given [GET] /stock-items")
    public class GivenGetStockItems {

        @Test
        @DisplayName("When called then it should return a 200 status")
        public void when_called_should_return_200_status() throws Exception {

            mockMvc.perform(get("/stock-items"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("When called then it should return an empty array")
        public void when_called_then_return_an_empty_array() throws Exception {

            mockMvc.perform(get("/stock-items").accept("application/json"))
                    .andExpect(content().json("[]"));
        }
    }
}
