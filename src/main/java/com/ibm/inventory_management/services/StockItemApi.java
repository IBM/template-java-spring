package com.ibm.inventory_management.services;

import java.util.List;

import com.ibm.inventory_management.models.StockItem;

public interface StockItemApi {
    List<StockItem> listStockItems() throws Exception;
}
