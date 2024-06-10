package com.example.database;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignController {

    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private Button signIn;
    @FXML
    private Button signUp;
    private Scene scene;
    private Stage stage;

    @FXML
    private void signIn(ActionEvent event) {
        String name = userName.getText();
        String passwordd = password.getText();

        String userPosition = authenticateUser(name, passwordd);
        if (userPosition != null) {
            try {
                switch (userPosition) {
                    case "Manager":
                        openManagerScreen(event);
                        break;
                    case "Cashier":
                        openCENTERScreen(event);
                        break;
                    default:
                        showAlert("Login Failed", "Access Denied.");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private void openCENTERScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("cashier.fxml"));
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Cashier Dashboard");
        stage.show();

        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void openManagerScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("centerScreen.fxml"));
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Manager Dashboard");
        stage.show();

        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private String authenticateUser(String userName, String password) {
        try (Connection connection = DatabaseConnection.connect()) {
            String sql = "SELECT employee_position FROM employee WHERE employee_name = ? AND employee_password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                UserSession.setUserPosition(resultSet.getString("employee_position"));
                return resultSet.getString("employee_position");

            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while connecting to the database.");
        }
        return null;
    }

    @FXML
    private void signUp(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUpScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Sign Up Screen");
        stage.show();

        Stage currentStage = (Stage) signUp.getScene().getWindow();
        currentStage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
