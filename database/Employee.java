package com.example.database;

import javafx.beans.property.*;

public class Employee {
    private IntegerProperty employeeId = new SimpleIntegerProperty(this, "employeeId");
    private StringProperty name = new SimpleStringProperty(this, "name");
    private StringProperty email = new SimpleStringProperty(this, "email");
    private StringProperty phone = new SimpleStringProperty(this, "phone");
    private StringProperty gender = new SimpleStringProperty(this, "gender");
    private StringProperty position = new SimpleStringProperty(this, "position");
    private StringProperty address = new SimpleStringProperty(this, "address");
    private DoubleProperty salary = new SimpleDoubleProperty(this, "salary");
    private StringProperty password = new SimpleStringProperty(this, "password");

    public Employee(int employeeId, String name, String email,
                    String phone, String position, String address,
                    double salary, String gender, String password) {
        this.employeeId.set(employeeId);
        this.name.set(name);
        this.email.set(email);
        this.phone.set(phone);
        this.position.set(position);
        this.address.set(address);
        this.salary.set(salary);
        this.password.set(password);
        this.gender.set(gender);
    }

    // Getters and setters
    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public int getEmployeeId() {
        return employeeId.get();
    }

    public IntegerProperty employeeIdProperty() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId.set(employeeId);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getPosition() {
        return position.get();
    }

    public StringProperty positionProperty() {
        return position;
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public double getSalary() {
        return salary.get();
    }

    public DoubleProperty salaryProperty() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary.set(salary);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
    @Override
    public String toString() {
        return getName();
    }
}
