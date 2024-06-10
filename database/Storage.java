package com.example.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Storage {
    private String storageName;
    private int storageId;
    private int storageCapacity;
    private String storageLocation;
    private ObservableList<Items> itemsList;

    public Storage(String storageName, int storageId, int storageCapacity, String storageLocation) {
        this.storageName = storageName;
        this.storageId = storageId;
        this.storageCapacity = storageCapacity;
        this.storageLocation = storageLocation;
        this.itemsList = FXCollections.observableArrayList();
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(int storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public int getStorageId() {
        return storageId;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public ObservableList<Items> getItemsList() {
        return itemsList;
    }

    public void setItemsList(ObservableList<Items> itemsList) {
        this.itemsList = itemsList;
    }
}
