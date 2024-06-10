package com.example.database;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerCENTER {
    @FXML
    private ImageView Customers;
    @FXML
    private ImageView Employees;
    @FXML
    private ImageView Suppliers;
    @FXML
    private ImageView Reorts;
    @FXML
    private ImageView Storage;
    @FXML
    private ImageView Items;

    Scene scene;

    @FXML
    private void customersScreen(MouseEvent event) {
        switchScene(event, "customer.fxml", "Customers Screen");
    }

    @FXML
    private void employeesScreen(MouseEvent event) {
        switchScene(event, "employee.fxml", "Employees Screen");
    }

    @FXML
    private void suppliersScreen(MouseEvent event) {
        switchScene(event, "Supplier.fxml", "Suppliers Screen");
    }


    @FXML
    private void reportsScreen(MouseEvent event) {
        switchScene(event, "report.fxml", "Reports Screen");
    }

    @FXML
    private void storageScreen(MouseEvent event) {
        switchScene(event, "Storage.fxml", "Storage Screen");
    }

    @FXML
    private void itemsScreen(MouseEvent event) {
        switchScene(event, "Items.fxml", "Items Screen");
    }

    @FXML
    private void logout(MouseEvent event) {
        switchScene(event, "Sign.fxml", "Sign In");
    }

    @FXML
    private void sellScreen(MouseEvent event) {
        switchScene(event, "Sell.fxml", "Sell Screen");
    }

    private void switchScene(MouseEvent event, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.INFORMATION, "Error", "An error occurred while opening the " + title.toLowerCase() + ".");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
