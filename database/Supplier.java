package com.example.database;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Supplier {
    private final SimpleIntegerProperty supplierId;
    private final SimpleStringProperty supplierName;
    private final SimpleStringProperty supplierAddress;
    private final SimpleIntegerProperty supplierPhone;
    private final SimpleStringProperty supplierEmail;

    public Supplier(int supplierId, String supplierName, String supplierAddress, int supplierPhone, String supplierEmail) {
        this.supplierId = new SimpleIntegerProperty(supplierId);
        this.supplierName = new SimpleStringProperty(supplierName);
        this.supplierAddress = new SimpleStringProperty(supplierAddress);
        this.supplierPhone = new SimpleIntegerProperty(supplierPhone);
        this.supplierEmail = new SimpleStringProperty(supplierEmail);
    }

    public int getSupplierId() {
        return supplierId.get();
    }

    public SimpleIntegerProperty supplierIdProperty() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public SimpleStringProperty supplierNameProperty() {
        return supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress.get();
    }

    public SimpleStringProperty supplierAddressProperty() {
        return supplierAddress;
    }

    public String getSupplierEmail() {
        return supplierEmail.get();
    }

    public SimpleStringProperty supplierEmailProperty() {
        return supplierEmail;
    }

    public int getSupplierPhone() {
        return supplierPhone.get();
    }

    public SimpleIntegerProperty supplierPhoneProperty() {
        return supplierPhone;
    }
}
