package com.ibm.inventory_management.models;

import java.io.Serializable;

public class StockItem implements Serializable {
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public StockItem withName(String name) {
        this.setName(name);
        return this;
    }
}
