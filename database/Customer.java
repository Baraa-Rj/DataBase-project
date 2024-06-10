package com.example.database;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    private IntegerProperty customerId = new SimpleIntegerProperty(this, "customerId");
    private StringProperty name = new SimpleStringProperty(this, "name");
    private StringProperty address = new SimpleStringProperty(this, "address");
    private StringProperty email = new SimpleStringProperty(this, "email");
    private IntegerProperty phone = new SimpleIntegerProperty(this, "phone");
    private ObservableList<Product> purchasedProducts = FXCollections.observableArrayList();

    public Customer(int customerId, String name, String address, String email, int phone) {
        this.customerId.set(customerId);
        this.name.set(name);
        this.address.set(address);
        this.email.set(email);
        this.phone.set(phone);
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty emailProperty() {
        return email;
    }


    public int getCustomerId() {
        return customerId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getEmail() {
        return email.get();
    }

    public int getPhone() {
        return phone.get();
    }

    public ObservableList<Product> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(ObservableList<Product> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPhone(int phone) {
        this.phone.set(phone);
    }
}
