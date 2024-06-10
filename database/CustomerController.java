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
import java.util.*;
import java.util.regex.Pattern;

public class CustomerController {
    @FXML
    private Button butBack;

    @FXML
    private Button butDone;

    @FXML
    private Button butShow;

    @FXML
    private CheckBox cbAddress;

    @FXML
    private CheckBox cbEmail;

    @FXML
    private CheckBox cbName;

    @FXML
    private CheckBox cbPhone;

    @FXML
    private RadioButton rbAdd;

    @FXML
    private RadioButton rbDelete;

    @FXML
    private RadioButton rbUpdate;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPhone;

    @FXML
    private TableView<Customer> tableView;

    @FXML
    private TableColumn<Customer, String> tcAddress;

    @FXML
    private TableColumn<Customer, String> tcEmail;

    @FXML
    private TableColumn<Customer, Integer> tcId;

    @FXML
    private TableColumn<Customer, String> tcName;

    @FXML
    private TableColumn<Customer, Integer> tcPhone;

    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        bindTableColumns();
        loadCustomers();
        setupInteraction();
    }

    private void bindTableColumns() {
        tcId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tableView.setItems(customers);
    }

    private void setupInteraction() {
        rbAdd.setOnAction(e -> clearFields());
        rbUpdate.setOnAction(e -> enableFields(false));
        rbDelete.setOnAction(e -> enableFields(true));
        butShow.setOnAction(e -> loadCustomers());
    }

    private void enableFields(boolean isDeleteSelected) {
        boolean disable = !isDeleteSelected;
        tfName.setDisable(disable);
        tfAddress.setDisable(disable);
        tfEmail.setDisable(disable);
        tfPhone.setDisable(disable);
        cbName.setDisable(disable);
        cbAddress.setDisable(disable);
        cbEmail.setDisable(disable);
        cbPhone.setDisable(disable);
        tfId.setDisable(!isDeleteSelected);
    }

    @FXML
    void onActionButtonClicked(ActionEvent event) {
        if (event.getSource() == butDone) {
            try {
                if (rbAdd.isSelected()) {
                    insertRecord();
                } else if (rbDelete.isSelected() && !tfId.getText().isEmpty()) {
                    deleteCustomer(Integer.parseInt(tfId.getText()));
                } else if (rbUpdate.isSelected()) {
                    updateCustomer();
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
            }
        } else if (event.getSource() == butBack) {
            butBack.getScene().getWindow().hide();
        }
    }

    @FXML
    private void BackButt(ActionEvent event) throws IOException {
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

    @FXML
    void checkName(ActionEvent event) {
        if (cbName.isSelected()) {
            cbPhone.setSelected(false);
            cbAddress.setSelected(false);
            cbEmail.setSelected(false);
            tfAddress.setDisable(true);
            tfPhone.setDisable(true);
            tfEmail.setDisable(true);
            tfName.setDisable(false);
        } else {
            tfName.setDisable(true);
        }
    }

    @FXML
    void checkAddress(ActionEvent event) {
        if (cbAddress.isSelected()) {
            cbPhone.setSelected(false);
            cbName.setSelected(false);
            cbEmail.setSelected(false);
            tfPhone.setDisable(true);
            tfName.setDisable(true);
            tfEmail.setDisable(true);
            tfAddress.setDisable(false);
        } else {
            tfAddress.setDisable(true);
        }
    }

    @FXML
    void checkEmail(ActionEvent event) {
        if (cbEmail.isSelected()) {
            cbPhone.setSelected(false);
            cbAddress.setSelected(false);
            cbName.setSelected(false);
            tfAddress.setDisable(true);
            tfName.setDisable(true);
            tfPhone.setDisable(true);
            tfEmail.setDisable(false);
        } else {
            tfEmail.setDisable(true);
        }
    }

    @FXML
    void checkPhone(ActionEvent event) {
        if (cbPhone.isSelected()) {
            cbName.setSelected(false);
            cbAddress.setSelected(false);
            cbEmail.setSelected(false);
            tfAddress.setDisable(true);
            tfName.setDisable(true);
            tfEmail.setDisable(true);
            tfPhone.setDisable(false);
        } else {
            tfPhone.setDisable(true);
        }
    }

    @FXML
    void selectAdd(ActionEvent event) {
        if (rbAdd.isSelected()) {
            rbUpdate.setSelected(false);
            rbDelete.setSelected(false);
            cbAddress.setDisable(true);
            cbEmail.setDisable(true);
            cbName.setDisable(true);
            cbPhone.setDisable(true);
            tfAddress.setDisable(false);
            tfEmail.setDisable(false);
            tfName.setDisable(false);
            tfPhone.setDisable(false);
            tfId.setDisable(true);
        }
    }

    @FXML
    void selectDelete(ActionEvent event) {
        if (rbDelete.isSelected()) {
            rbUpdate.setSelected(false);
            rbAdd.setSelected(false);
            cbAddress.setDisable(true);
            cbEmail.setDisable(true);
            cbName.setDisable(true);
            cbPhone.setDisable(true);
            tfAddress.setDisable(true);
            tfEmail.setDisable(true);
            tfName.setDisable(true);
            tfPhone.setDisable(true);
            tfId.setDisable(false);
        }
    }

    @FXML
    void selectUpdate(ActionEvent event) {
        if (rbUpdate.isSelected()) {
            rbDelete.setSelected(false);
            rbAdd.setSelected(false);
            cbAddress.setDisable(false);
            cbEmail.setDisable(false);
            cbName.setDisable(false);
            cbPhone.setDisable(false);
            tfAddress.setDisable(true);
            tfEmail.setDisable(true);
            tfName.setDisable(true);
            tfPhone.setDisable(true);
            tfId.setDisable(false);
        }
    }

    @FXML
    void showBut(ActionEvent event) {
        loadRecords();
    }

    private void insertRecord() throws SQLException {
        if (!validateInput()) return;
        String sql = "SELECT c.customer_id, c.customer_name, c.customer_address, " +
                "c.customer_email, cp.phone_number FROM customer c " +
                "LEFT JOIN customer_phone cp ON c.customer_id = cp.customer_id";

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tfName.getText());
            pstmt.setString(2, tfAddress.getText());
            pstmt.setString(3, tfEmail.getText());
            pstmt.setString(4, tfPhone.getText());
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully.");
            loadCustomers();
        }
    }

    private void executeUpdateQuery(int customerId, Customer customer, String sqlUpdate) throws SQLException {
        try (Connection connection = DatabaseConnection.connect(); PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();
        }
    }

    private void updateCustomer() throws SQLException {
        if (!validateInput() || tfId.getText().isEmpty()) return;
        String sql = "UPDATE customer SET customer_name = ?, customer_address = ?, customer_email = ?, phone_number = ? WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tfName.getText());
            pstmt.setString(2, tfAddress.getText());
            pstmt.setString(3, tfEmail.getText());
            pstmt.setString(4, tfPhone.getText());
            pstmt.setInt(5, Integer.parseInt(tfId.getText()));
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully.");
            loadCustomers();
        }
    }

    private Customer buildCustomerFromForm() {
        return new Customer(Integer.parseInt(tfId.getText()), tfName.getText(), tfAddress.getText(), tfEmail.getText(), Integer.parseInt(tfPhone.getText()));
    }

    private String buildUpdateQuery(Customer customer) {
        List<String> updates = new ArrayList<>();
        if (cbName.isSelected()) updates.add("customer_name = '" + customer.getName() + "'");
        if (cbAddress.isSelected()) updates.add("customer_address = '" + customer.getAddress() + "'");
        if (cbEmail.isSelected()) updates.add("customer_email = '" + customer.getEmail() + "'");
        return !updates.isEmpty() ? "UPDATE customer SET " + String.join(", ", updates) + " WHERE customer_id = ?" : "";
    }


    private void deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "No customer found with ID: " + customerId);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully.");
                loadCustomers();
            }
        }
    }

    private void loadCustomers() {
        String sql = "SELECT c.customer_id, c.customer_name, c.customer_address, " +
                "c.customer_email, cp.phone_number FROM customer c " +
                "LEFT JOIN customer_phone cp ON c.customer_id = cp.customer_id";
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            customers.clear();
            while (rs.next()) {
                customers.add(new Customer(rs.getInt("customer_id"), rs.getString("customer_name"), rs.getString("customer_address"), rs.getString("customer_email"), rs.getInt("phone_number")));
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load customers: " + ex.getMessage());
        }
    }

    private void loadRecords() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        String sql = "SELECT c.customer_id, c.customer_name, c.customer_address, " +
                "c.customer_email, cp.phone_number FROM customer c " +
                "LEFT JOIN customer_phone cp ON c.customer_id = cp.customer_id";
        Map<Integer, Customer> customerMap = new HashMap<>();
        try (Connection connection = DatabaseConnection.connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("customer_id");
                    String name = resultSet.getString("customer_name");
                    String address = resultSet.getString("customer_address");
                    String email = resultSet.getString("customer_email");
                    int phoneNumber = resultSet.getInt("phone_number");
                    Customer customer = customerMap.getOrDefault(id, new Customer(id, name, address, email, phoneNumber));
                    customerMap.put(id, customer);
                }
                customerList.addAll(customerMap.values());
                tableView.setItems(customerList);
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Error loading records", ex.getMessage());
        }
    }


    private boolean validateInput() {
        if (tfName.getText().isEmpty() || tfEmail.getText().isEmpty() || tfPhone.getText().isEmpty() || tfAddress.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled out.");
            return false;
        }

        if (!isValidName(tfName.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid name. Please enter a valid name.");
            return false;
        }

        if (!isValidEmail(tfEmail.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email. Please enter a valid email address.");
            return false;
        }

        if (!isValidPhone(tfPhone.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid phone number. Please enter a valid phone number.");
            return false;
        }

        return true;

    }

    private boolean isValidName(String name) {
        // Example: Check if the name contains only letters and spaces
        return name.matches("[a-zA-Z\\s]+");
    }

    private boolean isValidEmail(String email) {
        // Simple email pattern
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        // Check if the phone number contains exactly 10 digits
        return phone.matches("\\d{10}");
    }

    private void clearFields() {
        tfId.clear();
        tfName.clear();
        tfAddress.clear();
        tfEmail.clear();
        tfPhone.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
