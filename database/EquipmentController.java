package com.example.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class EquipmentController {
    @FXML
    private TableView<Product> tableView;
    @FXML
    private TableColumn<Product, Integer> productId;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Double> cost;
    @FXML
    private TableColumn<Product, Integer> quantity;
    @FXML
    TableColumn<Product, Integer> storageId;
    @FXML
    private TextField tfId, tfName, tfCost, tfQuantity, tfSupplier;
    @FXML
    Button btBack;
    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        productId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        storageId.setCellValueFactory(new PropertyValueFactory<>("storageId"));
        tableView.setItems(productList);
        loadProducts();
    }

    private void loadProducts() {
        String sql = "SELECT product_id, product_name, cost, quantity,storage_id FROM product";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                productList.add(new Product(rs.getString("product_name"), rs.getInt("product_id"),
                        rs.getInt("quantity"), rs.getDouble("cost"), rs.getInt("storage_id")));
            }
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load products: " + ex.getMessage());
        }
    }

    @FXML
    private void addProduct(ActionEvent event) {
        if (tfId.getText().isEmpty() || tfName.getText().isEmpty() || tfCost.getText().isEmpty() || tfQuantity.getText().isEmpty() || tfSupplier.getText().isEmpty() || tfId.getText().equals("0") || tfName.getText().equals("") || tfCost.getText().equals("") || tfQuantity.getText().equals("") || tfSupplier.getText().equals("")) {
            showAlert("Error", "Please fill in all fields");
            return;
        }
        String sql = "INSERT INTO product (product_id, product_name, cost, quantity,storage_id) VALUES (?, ?, ?, ?,?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(tfId.getText()));
            pstmt.setString(2, tfName.getText());
            pstmt.setDouble(3, Double.parseDouble(tfCost.getText()));
            pstmt.setInt(4, Integer.parseInt(tfQuantity.getText()));
            pstmt.setInt(5, Integer.parseInt(tfSupplier.getText()));
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                productList.add(new Product(tfName.getText(), Integer.parseInt(tfId.getText()),
                        Integer.parseInt(tfQuantity.getText()), Double.parseDouble(tfCost.getText()), Integer.parseInt(tfSupplier.getText())));
                clearFields();
                tableView.refresh();
            }
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to add product: " + ex.getMessage());
        }
    }

    @FXML
    private void updateProduct(ActionEvent event) {
        if (tfId.getText().isEmpty() || tfName.getText().isEmpty() || tfCost.getText().isEmpty() || tfQuantity.getText().isEmpty() || tfSupplier.getText().isEmpty() || tfId.getText().equals("0") || tfName.getText().equals("") || tfCost.getText().equals("") || tfQuantity.getText().equals("") || tfSupplier.getText().equals("")) {
            showAlert("Error", "Please select a product to update");
            return;
        }
        String sql = "UPDATE product SET product_name = ?, cost = ?, quantity = ?,storage_id=? WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tfName.getText());
            pstmt.setDouble(2, Double.parseDouble(tfCost.getText()));
            pstmt.setInt(3, Integer.parseInt(tfQuantity.getText()));
            pstmt.setInt(4, Integer.parseInt(tfId.getText()));
            pstmt.setInt(5, Integer.parseInt(tfSupplier.getText()));
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                productList.forEach(product -> {
                    if (product.getProductId() == Integer.parseInt(tfId.getText())) {
                        product.setProductName(tfName.getText());
                        product.setCost(Double.parseDouble(tfCost.getText()));
                        product.setQuantity(Integer.parseInt(tfQuantity.getText()));
                        product.setStorageId(Integer.parseInt(tfSupplier.getText()));
                        tableView.refresh();
                    }
                });
                clearFields();
            }
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to update product: " + ex.getMessage());
        }
    }

    @FXML
    private void deleteProduct(ActionEvent event) {
        if (tfId.getText().isEmpty() || tfName.getText().isEmpty() || tfCost.getText().isEmpty() || tfQuantity.getText().isEmpty() || tfSupplier.getText().isEmpty() || tfId.getText().equals("0") || tfName.getText().equals("") || tfCost.getText().equals("") || tfQuantity.getText().equals("") || tfSupplier.getText().equals("")) {
            showAlert("Error", "Please select a product to delete");
            return;
        }
        String sql = "DELETE FROM product WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(tfId.getText()));
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                productList.removeIf(product -> product.getProductId() == Integer.parseInt(tfId.getText()));
                clearFields();
                tableView.refresh();
            }
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to delete product: " + ex.getMessage());
        }
    }

    @FXML
    private void selectProduct() {
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            tfId.setText(String.valueOf(selectedProduct.getProductId()));
            tfName.setText(selectedProduct.getProductName());
            tfCost.setText(String.format("%.2f", selectedProduct.getCost()));
            tfQuantity.setText(String.valueOf(selectedProduct.getQuantity()));
            tfSupplier.setText(String.valueOf(selectedProduct.getStorageId()));
        }
    }

    private void clearFields() {
        tfId.clear();
        tfName.clear();
        tfCost.clear();
        tfQuantity.clear();
        tfSupplier.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        if (UserSession.getUserPosition().equals("Manager")) {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("centerScreen.fxml")));
            stage.setTitle("Center Screen");
            stage.setScene(new Scene(root));
            stage.show();
            ((Stage) tfId.getScene().getWindow()).close();
        } else {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("cashier.fxml")));
            stage.setTitle("Cashier Screen");
            stage.setScene(new Scene(root));
            stage.show();
            ((Stage) tfId.getScene().getWindow()).close();
        }
    }
}
