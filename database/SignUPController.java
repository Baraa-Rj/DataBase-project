package com.example.database;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUPController {

    @FXML
    private TextField employee_idfx;
    @FXML
    private TextField employee_namefx;
    @FXML
    private TextField employee_emailfx;
    @FXML
    private TextField employee_addressfx;
    @FXML
    private TextField employee_positionfx;
    @FXML
    private TextField storage_idfx;
    @FXML
    private Label poseition;
    @FXML
    private Label storg;
    @FXML
    private ImageView clear;
    @FXML
    private ImageView add;
    @FXML
    private ImageView back;
    @FXML
    private RadioButton adminRadio;
    @FXML
    private RadioButton MaleRadio;
    @FXML
    private RadioButton FemaleRadio;
    @FXML
    private RadioButton cashierRadio;
    private ToggleGroup typeToggleGroup;
    private ToggleGroup genderToggleGroup;
    private Stage stage;

    @FXML
    private void initialize() {
        typeToggleGroup = new ToggleGroup();
        adminRadio.setToggleGroup(typeToggleGroup);
        cashierRadio.setToggleGroup(typeToggleGroup);

        typeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                String type = selectedRadioButton.getText();
                employee_positionfx.setText(type);
            }
        });

        genderToggleGroup = new ToggleGroup();
        FemaleRadio.setToggleGroup(genderToggleGroup);
        MaleRadio.setToggleGroup(genderToggleGroup);
    }

    @FXML
    private void clearFields(MouseEvent event) {
        employee_idfx.clear();
        employee_namefx.clear();
        employee_emailfx.clear();
        employee_addressfx.clear();
        employee_positionfx.clear();
        storage_idfx.clear();
        typeToggleGroup.selectToggle(null);
        genderToggleGroup.selectToggle(null);
    }

    @FXML
    private void addEmployee(MouseEvent event) {
        String employee_id = employee_idfx.getText();
        String employee_name = employee_namefx.getText();
        String employee_email = employee_emailfx.getText();
        String employee_address = employee_addressfx.getText();
        String employee_position = employee_positionfx.getText();
        String storage_id = storage_idfx.getText();

        if (employee_id.isEmpty() || employee_name.isEmpty() || employee_email.isEmpty() || employee_address.isEmpty()
                || employee_position.isEmpty() || storage_id.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all fields");
            return;
        }

        if (!isNumeric(employee_id)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "ID must be a number");
            return;
        }

        if (!isNumeric(storage_id)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Storage ID must be a number");
            return;
        }

        if (!isValidEmail(employee_email)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid email address");
            return;
        }

        if (!isAlpha(employee_name)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Name must contain only letters and spaces");
            return;
        }

        if (!isValidPosition(employee_position)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Position must be either 'admin' or 'cashier'");
            return;
        }

        String gender = "";
        RadioButton selectedGender = (RadioButton) genderToggleGroup.getSelectedToggle();
        if (selectedGender != null) {
            gender = selectedGender.getText();
        }

        String sql = "INSERT INTO employee (employee_id, employee_name, employee_email, employee_address, employee_position, employee_password, employee_gender) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(employee_id));
                pstmt.setString(2, employee_name);
                pstmt.setString(3, employee_email);
                pstmt.setString(4, employee_address);
                pstmt.setString(5, employee_position);
                pstmt.setInt(6, Integer.parseInt(storage_id));
                pstmt.setString(7, gender);

                pstmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee added successfully!");

            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add employee: " + e.getMessage());
        }
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }

    private boolean isAlpha(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    private boolean isValidPosition(String position) {
        return "admin".equalsIgnoreCase(position) || "cashier".equalsIgnoreCase(position);
    }

    @FXML
    private void goBack(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Sign.fxml"));
            Parent root = loader.load();


            Stage currentStage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();


            Scene mainScene = new Scene(root);


            currentStage.setScene(mainScene);
            currentStage.setTitle("Sign Screen");
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.INFORMATION, "Error", "An error occurred while opening the main screen.");
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
