package com.example.database;

public class Product extends Items {
    public Product(String itemName, int itemId, int itemQuantity, double cost, int storageId) {
        super(itemId, itemName, cost, itemQuantity, storageId);
    }

    @Override
    public String toString() {
        return getProductName();
    }
}
