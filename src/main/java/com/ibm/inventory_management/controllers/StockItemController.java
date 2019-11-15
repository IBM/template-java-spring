package com.ibm.inventory_management.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockItemController {

    @GetMapping(path = "/stock-items", produces = "application/json")
    public List listStockItems() {
        return new ArrayList();
    }
}
