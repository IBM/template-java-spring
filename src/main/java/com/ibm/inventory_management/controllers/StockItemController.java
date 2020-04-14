package com.ibm.inventory_management.controllers;

import java.util.ArrayList;
import java.util.List;

import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.inventory_management.models.StockItem;
import com.ibm.inventory_management.services.StockItemApi;

@RestController
public class StockItemController {

    private final StockItemApi service;
    private final Tracer tracer;

    public StockItemController(StockItemApi service, Tracer tracer) {
        this.service = service;
        this.tracer = tracer;
    }

    @GetMapping(path = "/stock-items", produces = "application/json")
    public List<StockItem> listStockItems() {
        Span span = this.tracer.buildSpan("stock-items").start();

        List<StockItem> items = new ArrayList<StockItem>();
        try {
            items = this.service.listStockItems();

            span.log("Got items: " + items);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            span.finish();
        }

        return items;
    }
}
