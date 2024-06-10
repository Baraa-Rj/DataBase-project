package com.example.database;

public class Equipment extends Items {
    public Equipment(int itemId, String itemName, int itemQuantity, double cost, int storageId) {
        super(itemId, itemName, cost, itemQuantity, storageId);
    }
}
