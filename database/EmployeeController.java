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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class EmployeeController {

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
    private CheckBox cbGender;

    @FXML
    private CheckBox cbName;

    @FXML
    private CheckBox cbPassword;

    @FXML
    private CheckBox cbPhone;

    @FXML
    private CheckBox cbSalary;

    @FXML
    private CheckBox cbtype;

    @FXML
    private RadioButton rbAdd;

    @FXML
    private RadioButton rbCasher;

    @FXML
    private RadioButton rbDelete;

    @FXML
    private RadioButton rbFemale;

    @FXML
    private RadioButton rbMale;

    @FXML
    private RadioButton rbManager;

    @FXML
    private RadioButton rbUpdate;

    @FXML
    private TableView<Employee> tableView;

    @FXML
    private TableColumn<Employee, String> tcAddress;

    @FXML
    private TableColumn<Employee, String> tcEmail;

    @FXML
    private TableColumn<Employee, String> tcGender;

    @FXML
    private TableColumn<Employee, Integer> tcId;

    @FXML
    private TableColumn<Employee, String> tcName;

    @FXML
    private TableColumn<Employee, String> tcPassword;

    @FXML
    private TableColumn<Employee, String> tcPhone;

    @FXML
    private TableColumn<Employee, Integer> tcSalary;

    @FXML
    private TableColumn<Employee, String> tcType;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfPhone;

    @FXML
    private TextField tfSalary;

    @FXML
    void BackButt(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("centerScreen.fxml")));
        stage.setTitle("Center Screen");
        stage.setScene(new Scene(root));
        stage.show();
        butBack.getScene().getWindow().hide();

    }


    @FXML
    void DoneBut(ActionEvent event) throws SQLException {
        if (rbAdd.isSelected()) {
            insertRecord();
        } else if (rbDelete.isSelected()) {
            String idText = tfId.getText();
            if (!idText.isEmpty()) {
                try {
                    int customerId = Integer.parseInt(idText);
                    deleteEmployee(customerId);
                } catch (NumberFormatException e) {
                    showAlert(1, "Error", "Invalid ID format. Please enter a valid integer.");
                }
            } else {
                showAlert(1, "Error", "Please enter a customer ID.");
            }
        } else if (rbUpdate.isSelected()) {
            updateEmployee();
        } else {
            showAlert(1, "Error", "Please select your choice.");
        }
    }

    @FXML
    void CheckType(ActionEvent event) {
        if (cbtype.isSelected()) {
            cbPhone.setSelected(false);
            cbAddress.setSelected(false);
            cbEmail.setSelected(false);
            cbGender.setSelected(false);
            cbName.setSelected(false);
            cbSalary.setSelected(false);
            cbPassword.setSelected(false);
            tfAddress.setDisable(true);
            tfPhone.setDisable(true);
            tfEmail.setDisable(true);
            tfPassword.setDisable(true);
            tfSalary.setDisable(true);
            rbFemale.setDisable(true);
            rbMale.setDisable(true);
            rbManager.setDisable(false);
            rbCasher.setDisable(false);
            tfName.setDisable(true);
        } else {
            rbManager.setDisable(true);
            rbCasher.setDisable(true);
        }
    }

    @FXML
    void cheakName(ActionEvent event) {
        if (cbName.isSelected()) {
            cbPhone.setSelected(false);
            cbAddress.setSelected(false);
            cbEmail.setSelected(false);
            cbGender.setSelected(false);
            cbtype.setSelected(false);
            cbSalary.setSelected(false);
            cbPassword.setSelected(false);
            tfAddress.setDisable(true);
            tfPhone.setDisable(true);
            tfEmail.setDisable(true);
            tfPassword.setDisable(true);
            tfSalary.setDisable(true);
            rbFemale.setDisable(true);
            rbMale.setDisable(true);
            rbManager.setDisable(true);
            rbCasher.setDisable(true);
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
            cbGender.setSelected(false);
            cbtype.setSelected(false);
            cbSalary.setSelected(false);
            cbPassword.setSelected(false);
            tfAddress.setDisable(false);
            tfPhone.setDisable(true);
            tfEmail.setDisable(true);
            tfPassword.setDisable(true);
            tfSalary.setDisable(true);
            rbFemale.setDisable(true);
            rbMale.setDisable(true);
            rbManager.setDisable(true);
            rbCasher.setDisable(true);
            tfName.setDisable(true);
        } else {
            tfAddress.setDisable(true);
        }
    }

    @FXML
    void checkEmail(ActionEvent event) {
        if (cbEmail.isSelected()) {
            cbPhone.setSelected(false);
            cbName.setSelected(false);
            cbAddress.setSelected(false);
            cbGender.setSelected(false);
            cbtype.setSelected(false);
            cbSalary.setSelected(false);
            cbPassword.setSelected(false);
            tfAddress.setDisable(true);
            tfPhone.setDisable(true);
            tfEmail.setDisable(false);
            tfPassword.setDisable(true);
            tfSalary.setDisable(true);
            rbFemale.setDisable(true);
            rbMale.setDisable(true);
            rbManager.setDisable(true);
            rbCasher.setDisable(true);
            tfName.setDisable(true);
        } else {
            tfEmail.setDisable(true);
        }
    }

    @FXML
    void checkGender(ActionEvent event) {
        if (cbGender.isSelected()) {
            cbPhone.setSelected(false);
            cbName.setSelected(false);
            cbAddress.setSelected(false);
            cbEmail.setSelected(false);
            cbtype.setSelected(false);
            cbSalary.setSelected(false);
            cbPassword.setSelected(false);
            tfAddress.setDisable(true);
            tfPhone.setDisable(true);
            tfEmail.setDisable(true);
            tfPassword.setDisable(true);
            tfSalary.setDisable(true);
            rbFemale.setDisable(false);
            rbMale.setDisable(false);
            rbManager.setDisable(true);
            rbCasher.setDisable(true);
            tfName.setDisable(false);
        } else {
            rbFemale.setDisable(true);
            rbMale.setDisable(true);
        }
    }

    @FXML
    void checkPassword(ActionEvent event) {
        if (cbPassword.isSelected()) {
            cbPhone.setSelected(false);
            cbName.setSelected(false);
            cbAddress.setSelected(false);
            cbEmail.setSelected(false);
            cbtype.setSelected(false);
            cbSalary.setSelected(false);
            cbGender.setSelected(false);
            tfAddress.setDisable(true);
            tfPhone.setDisable(true);
            tfEmail.setDisable(true);
            tfPassword.setDisable(false);
            tfSalary.setDisable(true);
            rbFemale.setDisable(true);
            rbMale.setDisable(true);
            rbManager.setDisable(true);
            rbCasher.setDisable(true);
            tfName.setDisable(true);
        } else {
            tfPassword.setDisable(true);
        }
    }

    @FXML
    void checkPhone(ActionEvent event) {
        if (cbPhone.isSelected()) {
            cbPassword.setSelected(false);
            cbName.setSelected(false);
            cbAddress.setSelected(false);
            cbEmail.setSelected(false);
            cbtype.setSelected(false);
            cbSalary.setSelected(false);
            cbGender.setSelected(false);
            tfAddress.setDisable(true);
            tfPhone.setDisable(false);
            tfEmail.setDisable(true);
            tfPassword.setDisable(true);
            tfSalary.setDisable(true);
            rbFemale.setDisable(true);
            rbMale.setDisable(true);
            rbManager.setDisable(true);
            rbCasher.setDisable(true);
            tfName.setDisable(true);
        } else {
            tfPhone.setDisable(true);
        }
    }

    @FXML
    void checkSalary(ActionEvent event) {
        if (cbSalary.isSelected()) {
            cbPassword.setSelected(false);
            cbName.setSelected(false);
            cbAddress.setSelected(false);
            cbEmail.setSelected(false);
            cbtype.setSelected(false);
            cbPhone.setSelected(false);
            cbGender.setSelected(false);
            tfAddress.setDisable(true);
            tfPhone.setDisable(true);
            tfEmail.setDisable(true);
            tfPassword.setDisable(true);
            tfSalary.setDisable(false);
            rbFemale.setDisable(true);
            rbMale.setDisable(true);
            rbManager.setDisable(true);
            rbCasher.setDisable(true);
            tfName.setDisable(true);
        } else {
            tfSalary.setDisable(true);
        }
    }

    @FXML
    void selectAdd(ActionEvent event) {
        if (rbAdd.isSelected()) {
            rbUpdate.setSelected(false);
            rbDelete.setSelected(false);

            cbPassword.setSelected(false);
            cbName.setSelected(false);
            cbAddress.setSelected(false);
            cbEmail.setSelected(false);
            cbtype.setSelected(false);
            cbSalary.setSelected(false);
            cbGender.setSelected(false);
            cbPhone.setSelected(false);

            cbPassword.setDisable(true);
            cbName.setDisable(true);
            cbAddress.setDisable(true);
            cbEmail.setDisable(true);
            cbtype.setDisable(true);
            cbSalary.setDisable(true);
            cbGender.setDisable(true);
            cbPhone.setDisable(true);

            tfAddress.setDisable(false);
            tfPhone.setDisable(false);
            tfEmail.setDisable(false);
            tfPassword.setDisable(false);
            tfSalary.setDisable(false);
            rbFemale.setDisable(false);
            rbMale.setDisable(false);
            rbManager.setDisable(false);
            rbCasher.setDisable(false);
            tfName.setDisable(false);
            tfId.setDisable(true);
        }
    }

    @FXML
    void selectCashire(ActionEvent event) {
        if (rbCasher.isSelected()) {
            rbManager.setSelected(false);
        }
    }

    @FXML
    void selectDelete(ActionEvent event) {
        if (rbDelete.isSelected()) {
            rbUpdate.setSelected(false);
            rbAdd.setSelected(false);

            cbPassword.setSelected(false);
            cbName.setSelected(false);
            cbAddress.setSelected(false);
            cbEmail.setSelected(false);
            cbtype.setSelected(false);
            cbSalary.setSelected(false);
            cbGender.setSelected(false);
            cbPhone.setSelected(false);

            cbPassword.setDisable(true);
            cbName.setDisable(true);
            cbAddress.setDisable(true);
            cbEmail.setDisable(true);
            cbtype.setDisable(true);
            cbSalary.setDisable(true);
            cbGender.setDisable(true);
            cbPhone.setDisable(true);

            tfAddress.setDisable(true);
            tfPhone.setDisable(true);
            tfEmail.setDisable(true);
            tfPassword.setDisable(true);
            tfSalary.setDisable(true);
            rbFemale.setDisable(true);
            rbMale.setDisable(true);
            rbManager.setDisable(true);
            rbCasher.setDisable(true);
            tfName.setDisable(true);
            tfId.setDisable(false);
        }
    }

    @FXML
    void selectFemale(ActionEvent event) {
        if (rbFemale.isSelected()) {
            rbMale.setSelected(false);
        }
    }

    @FXML
    void selectMale(ActionEvent event) {
        if (rbMale.isSelected()) {
            rbFemale.setSelected(false);
        }
    }

    @FXML
    void selectManager(ActionEvent event) {
        if (rbManager.isSelected()) {
            rbCasher.setSelected(false);
        }
    }

    @FXML
    void selectUpdate(ActionEvent event) {
        if (rbUpdate.isSelected()) {
            rbDelete.setSelected(false);
            rbAdd.setSelected(false);

            cbPassword.setSelected(false);
            cbName.setSelected(false);
            cbAddress.setSelected(false);
            cbEmail.setSelected(false);
            cbtype.setSelected(false);
            cbSalary.setSelected(false);
            cbGender.setSelected(false);
            cbPhone.setSelected(false);

            cbPassword.setDisable(false);
            cbName.setDisable(false);
            cbAddress.setDisable(false);
            cbEmail.setDisable(false);
            cbtype.setDisable(false);
            cbSalary.setDisable(false);
            cbGender.setDisable(false);
            cbPhone.setDisable(false);

            tfAddress.setDisable(true);
            tfPhone.setDisable(true);
            tfEmail.setDisable(true);
            tfPassword.setDisable(true);
            tfSalary.setDisable(true);
            rbFemale.setDisable(true);
            rbMale.setDisable(true);
            rbManager.setDisable(true);
            rbCasher.setDisable(true);
            tfName.setDisable(true);
            tfId.setDisable(false);
        }
    }

    private void insertRecord() throws SQLException {
        if (validateInput()) {
            try (Connection connection = DatabaseConnection.connect()) {
                String newPassword = tfPassword.getText();
                String newEmail = tfEmail.getText();
                String sql = "INSERT INTO employee (employee_name, employee_email, employee_address, employee_position, salary, employee_password, employee_gender) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, tfName.getText());
                preparedStatement.setString(2, newEmail);
                preparedStatement.setString(3, tfAddress.getText());
                preparedStatement.setString(4, rbManager.isSelected() ? "Manager" : "Cashier");
                preparedStatement.setInt(5, Integer.parseInt(tfSalary.getText()));
                preparedStatement.setString(6, newPassword);
                preparedStatement.setString(7, rbMale.isSelected() ? "Male" : "Female");
                preparedStatement.executeUpdate();
                // Get the generated employee_id
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                int employeeId = 0;
                if (resultSet.next()) {
                    employeeId = resultSet.getInt(1);
                }

                // Insert into employee_phone table
                sql = "INSERT INTO employee_phone (employee_id, phone_number) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, employeeId);
                preparedStatement.setString(2, tfPhone.getText());
                preparedStatement.executeUpdate();

                // Load records and clear fields
                loadRecords();
                clearFields();
                showAlert(2, "Success", "Record inserted successfully.");
            } catch (SQLException ex) {
                showAlert(1, "Error inserting record", ex.getMessage());
            }
        }
    }


    @FXML
    void updateEmployee() {
        String employeeIdText = tfId.getText();
        if (employeeIdText.isEmpty()) {
            showAlert("Error", "Please enter an employee ID.");
            return;
        }

        try {
            int employeeId = Integer.parseInt(employeeIdText);

            try (Connection connection = DatabaseConnection.connect()) {
                if (updateEmployeeDetails(connection, employeeId)) {
                    showAlert("Success", "Employee updated successfully.");
                    loadRecords();
                    clearFields();
                }
            } catch (SQLException ex) {
                showAlert("Error updating record", ex.getMessage());
            }
        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid employee ID format.");
        }
    }

    private boolean updateEmployeeDetails(Connection connection, int employeeId) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE employee SET ");
        List<Object> params = new ArrayList<>();

        if (cbName.isSelected()) {
            sqlBuilder.append("employee_name = ?, ");
            params.add(tfName.getText());
        }
        if (cbAddress.isSelected()) {
            sqlBuilder.append("employee_address = ?, ");
            params.add(tfAddress.getText());
        }
        if (cbEmail.isSelected()) {
            sqlBuilder.append("employee_email = ?, ");
            params.add(tfEmail.getText());
        }
        if (cbGender.isSelected()) {
            sqlBuilder.append("employee_gender = ?, ");
            params.add(rbMale.isSelected() ? "Male" : "Female");
        }
        if (cbPassword.isSelected()) {
            if (!isPasswordValid(tfPassword.getText())) {
                showAlert("Validation Error", "Password does not meet the security criteria.");
                return false;
            }
            sqlBuilder.append("employee_password = ?, ");
            params.add(tfPassword.getText());
        }
        if (cbtype.isSelected()) {
            sqlBuilder.append("employee_position = ?, ");
            params.add(rbManager.isSelected() ? "Manager" : "Cashier");
        }
        if (cbSalary.isSelected()) {
            sqlBuilder.append("salary = ?, ");
            params.add(Integer.parseInt(tfSalary.getText()));
        }
        if (cbPhone.isSelected()) {
            updateEmployeePhone(connection, employeeId, tfPhone.getText());
        }

        if (params.isEmpty()) {
            showAlert("Error", "No fields selected for update.");
            return false;
        }

        sqlBuilder.setLength(sqlBuilder.length() - 2); // Remove the last comma and space
        sqlBuilder.append(" WHERE employee_id = ?");
        params.add(employeeId);

        try (PreparedStatement stmt = connection.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            stmt.executeUpdate();
        }
        return true;
    }

    private void updateEmployeePhone(Connection connection, int employeeId, String phone) throws SQLException {
        String sql = "UPDATE employee_phone SET phone_number = ? WHERE employee_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);
            stmt.setInt(2, employeeId);
            stmt.executeUpdate();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void deleteEmployee(int employeeId) {
        try (Connection connection = DatabaseConnection.connect()) {
            // Delete from customer_phone table
            String sql = "DELETE FROM employee_phone WHERE employee_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employeeId);
            int phoneRowsAffected = preparedStatement.executeUpdate();

            // Delete from customer table
            sql = "DELETE FROM employee WHERE employee_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employeeId);
            int customerRowsAffected = preparedStatement.executeUpdate();

            if (phoneRowsAffected == 0 && customerRowsAffected == 0) {
                // No rows affected means the customer ID does not exist in the database
                showAlert(1, "Error", "No employee found with ID: " + employeeId);
                clearFields();
            } else {
                loadRecords();
                clearFields(); // Clear input fields after deleting
                showAlert(2, "Success", "Employee deleted successfully.");
            }
        } catch (SQLException ex) {
            showAlert(1, "Error deleting record", ex.getMessage());
        }

    }

    private void loadRecords() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        String query = "SELECT e.employee_id, e.employee_name, e.employee_address, e.employee_email, e.employee_position, e.salary, e.employee_password, e.employee_gender, ep.phone_number "
                + "FROM employee e LEFT JOIN employee_phone ep ON e.employee_id = ep.employee_id";

        try (Connection connection = DatabaseConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("employee_id");
                String name = resultSet.getString("employee_name");
                String address = resultSet.getString("employee_address");
                String email = resultSet.getString("employee_email");
                String position = resultSet.getString("employee_position");
                int salary = resultSet.getInt("salary");
                String password = resultSet.getString("employee_password");
                String gender = resultSet.getString("employee_gender");
                String phone = resultSet.getString("phone_number");
                System.out.println(password);
                Employee employee = new Employee(id, name, email, phone, position, address, salary, gender, password);

                employeeList.add(employee);
            }

            tableView.getColumns().clear();

            TableColumn<Employee, Integer> tcId = new TableColumn<>("ID");
            tcId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));

            TableColumn<Employee, String> tcName = new TableColumn<>("Name");
            tcName.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Employee, String> tcAddress = new TableColumn<>("Address");
            tcAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

            TableColumn<Employee, String> tcEmail = new TableColumn<>("Email");
            tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

            TableColumn<Employee, String> tcPosition = new TableColumn<>("Position");
            tcPosition.setCellValueFactory(new PropertyValueFactory<>("position"));

            TableColumn<Employee, Integer> tcSalary = new TableColumn<>("Salary");
            tcSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

            TableColumn<Employee, String> tcGender = new TableColumn<>("Gender");
            tcGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

            TableColumn<Employee, String> tcPhone = new TableColumn<>("Phone");
            tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

            TableColumn<Employee, String> tcPassword = new TableColumn<>("Password");
            tcPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
            tableView.getColumns().add(tcId);
            tableView.getColumns().add(tcName);
            tableView.getColumns().add(tcAddress);
            tableView.getColumns().add(tcEmail);
            tableView.getColumns().add(tcPosition);
            tableView.getColumns().add(tcSalary);
            tableView.getColumns().add(tcGender);
            tableView.getColumns().add(tcPhone);
            tableView.getColumns().add(tcPassword);
            tableView.setItems(employeeList);

        } catch (SQLException ex) {
            showAlert(1, "Error loading records", ex.getMessage());
        }
    }

    private boolean validateInput() {
        if (tfName.getText().isEmpty() || tfEmail.getText().isEmpty() || tfPhone.getText().isEmpty()
                || tfAddress.getText().isEmpty() || tfSalary.getText().isEmpty()
                || !isGenderValid() || !isTypeValid() || tfPassword.getText().isEmpty()) {
            showAlert(1, "Validation Error", "All fields must be filled out.");
            return false;
        }

        if (!isValidName(tfName.getText())) {
            showAlert(1, "Validation Error", "Invalid name. Please enter a valid name.");
            return false;
        }

        if (!isValidEmail(tfEmail.getText())) {
            showAlert(1, "Validation Error", "Invalid email. Please enter a valid email address.");
            return false;
        }

        if (!isValidPhone(tfPhone.getText())) {
            showAlert(1, "Validation Error", "Invalid phone number. Please enter a valid phone number.");
            return false;
        }

        if (!isSalaryValid(tfSalary.getText())) {
            showAlert(1, "Validation Error", "Invalid salary. Please enter a positive number.");
            return false;
        }

        if (!isPasswordValid(tfPassword.getText())) {
            showAlert(1, "Validation Error", "Invalid password. Ensure it meets the criteria.");
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

    private boolean isPasswordValid(String password) {
        // Example criteria: at least 8 characters long
        if (password == null || password.length() < 8) {
            return false;
        }
        // You can add more criteria such as requiring a mix of letters, numbers, and special characters
        return true;
    }

    private boolean isSalaryValid(String salaryText) {
        try {
            int salary = Integer.parseInt(salaryText);
            return salary > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean isGenderValid() {
        return rbMale.isSelected() || rbFemale.isSelected();
    }

    private boolean isTypeValid() {
        return rbManager.isSelected() || rbCasher.isSelected();
    }

    private void clearFields() {
        tfId.clear();
        tfName.clear();
        tfAddress.clear();
        tfEmail.clear();
        tfPhone.clear();
        tfPassword.clear();
        tfSalary.clear();
        rbMale.setSelected(false);
        rbFemale.setSelected(false);
        rbManager.setSelected(false);
        rbCasher.setSelected(false);
    }

    private void showAlert(int alertType, String title, String message) {
        Alert alert;
        if (alertType == 1) {
            alert = new Alert(Alert.AlertType.ERROR);
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void showButt(ActionEvent event) {
        loadRecords();
    }

}