package com.ibm.inventory_management.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.inventory_management.models.StockItem;
import com.ibm.inventory_management.services.StockItemApi;

@RestController
public class StockItemController {

    private final StockItemApi service;

    public StockItemController(StockItemApi service) {
        this.service = service;
    }

    @GetMapping(path = "/stock-items", produces = "application/json")
    public List<StockItem> listStockItems() {
        List<StockItem> items = new ArrayList<StockItem>();
        try {
            items = this.service.listStockItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
