package com.ibm.inventory_management.models;

import java.io.Serializable;

public class StockItem implements Serializable {
    private String name;
    private String id = null;
    private int stock = 0;
    private double price = 0.0;
    private String manufacturer = "";

    public StockItem() {
        super();
    }

    public StockItem(String id) {
        this.id = id;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StockItem withId(String id) {
        this.setId(id);
        return this;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public StockItem withStock(int stock) {
        this.setStock(stock);
        return this;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public StockItem withPrice(double price) {
        this.setPrice(price);
        return this;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public StockItem withManufacturer(String manufacturer) {
        this.setManufacturer(manufacturer);
        return this;
    }
}
