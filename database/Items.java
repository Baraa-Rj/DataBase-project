package com.example.database;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Items {
    private SimpleIntegerProperty productId;
    private SimpleStringProperty productName;
    private SimpleDoubleProperty cost;
    private SimpleIntegerProperty quantity;
    private SimpleIntegerProperty storageId;

    public Items(int productId, String productName, double cost, int quantity, int storageId) {
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.cost = new SimpleDoubleProperty(cost);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.storageId = new SimpleIntegerProperty(storageId);
    }

    // Getters and Setters
    public int getProductId() { return productId.get(); }
    public String getProductName() { return productName.get(); }
    public double getCost() { return cost.get(); }
    public int getQuantity() { return quantity.get(); }
    public int getStorageId() { return storageId.get(); }

    public void setProductId(int id) { this.productId.set(id); }
    public void setProductName(String name) { this.productName.set(name); }
    public void setCost(double cost) { this.cost.set(cost); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
    public void setStorageId(int storageId) { this.storageId.set(storageId); }
}
