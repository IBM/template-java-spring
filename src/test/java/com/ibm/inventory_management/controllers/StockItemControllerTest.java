package com.ibm.inventory_management.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ibm.inventory_management.models.StockItem;
import com.ibm.inventory_management.services.StockItemApi;

@DisplayName("StockItemController")
public class StockItemControllerTest {
    StockItemController controller;
    StockItemApi service;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        service = mock(StockItemApi.class);

        controller = spy(new StockItemController(service));

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

        @Test
        @DisplayName("When called then it should return the results of the StockItemService")
        public void when_called_then_return_the_results_of_the_stockitemservice() throws Exception {

            final List<StockItem> expectedResult = Arrays.asList(new StockItem());
            when(service.listStockItems()).thenReturn(expectedResult);

            mockMvc.perform(get("/stock-items").accept("application/json"))
                    .andExpect(content().json("[{}]"));
        }
    }
}
