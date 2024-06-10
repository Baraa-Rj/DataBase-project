package com.example.database;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SellController {
    @FXML
    private TableView<Customer> tableView;
    @FXML
    private TableColumn<Customer, Integer> customerIDColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private Button btSell;
    @FXML
    private ComboBox<Employee> employeeComboBox;
    @FXML
    private Button btBack;

    private ObservableList<Customer> customerData = FXCollections.observableArrayList();
    private ObservableList<Product> productData = FXCollections.observableArrayList();
    private ObservableList<Employee> employeeData = FXCollections.observableArrayList();
    private TableView<Product> productsTable;
    private TableColumn<Product, String> productNameColumn;
    private TableColumn<Product, Integer> productPriceColumn;

    @FXML
    public void initialize() throws SQLException {
        setupTableColumns();
        setupComboBoxes();
        loadData();
        setupEventHandlers();
    }

    private void loadData() {
        loadCustomers();
        loadEmployees();
        try {
            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupComboBoxes() {
        productComboBox.setItems(productData);
        employeeComboBox.setItems(employeeData);

        productComboBox.setCellFactory(lv -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });

        productComboBox.setButtonCell(new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });

        employeeComboBox.setCellFactory(lv -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });

        employeeComboBox.setButtonCell(new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });
    }


    private void setupTableColumns() {
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    private void setupEventHandlers() {
        btSell.setOnAction(event -> sellProductAction());
        tableView.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    showPurchasedProductsDialog(row.getItem());
                }
            });
            return row;
        });
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        if (UserSession.getUserPosition().equals("Manager")) {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("centerScreen.fxml")));
            stage.setTitle("Center Screen");
            stage.setScene(new Scene(root));
            stage.show();
            ((Stage) btBack.getScene().getWindow()).close();
        } else {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("cashier.fxml")));
            stage.setTitle("Cashier Screen");
            stage.setScene(new Scene(root));
            stage.show();
            ((Stage) btBack.getScene().getWindow()).close();
        }
    }

    private void loadCustomers() {
        String query = "SELECT c.customer_id, c.customer_name, c.customer_address, c.customer_email, p.phone_number " +
                "FROM customer c " +
                "LEFT JOIN customer_phone p ON c.customer_id = p.customer_id";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ObservableList<Customer> customers = FXCollections.observableArrayList();
            Map<Integer, Customer> customerMap = new HashMap<>();
            while (rs.next()) {
                int id = rs.getInt("customer_id");
                Customer customer = customerMap.get(id);
                if (customer == null) {
                    customer = new Customer(
                            id,
                            rs.getString("customer_name"),
                            rs.getString("customer_address"),
                            rs.getString("customer_email"),
                            rs.getInt("phone_number")
                    );
                    customerMap.put(id, customer);
                    customers.add(customer);
                } else {
                    int existingPhones = customer.getPhone();
                    customer.setPhone(rs.getInt("phone_number"));
                }
            }
            customerData.setAll(customers);
            tableView.setItems(customerData);
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load customers: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadProducts() {
        String query = "SELECT product_id, product_name, cost, quantity, storage_id FROM product";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ObservableList<Product> products = FXCollections.observableArrayList();
            while (rs.next()) {
                products.add(new Product(
                        rs.getString("product_name"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("cost"),
                        rs.getInt("storage_id")
                ));
            }
            Platform.runLater(() -> {
                productData.setAll(products);
                productComboBox.setItems(productData);
            });
        } catch (SQLException ex) {
            System.err.println("Failed to load products: " + ex.getMessage());
        }
    }


    private void updateProductStock(Product product, int newQuantity) throws SQLException {
        String updateQuery = "UPDATE product SET quantity = ? WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, product.getProductId());
            pstmt.executeUpdate();

            // Refresh product list safely
            Platform.runLater(() -> {
                try {
                    loadProducts();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException ex) {
            System.err.println("Failed to update product stock: " + ex.getMessage());
        }
    }


    private void loadEmployees() {

        ObservableList<Employee> employees = FXCollections.observableArrayList();
        String query = "SELECT e.employee_id, e.employee_name, e.employee_email,  e.employee_position, e.employee_address," +
                " p.phone_number,e.salary,e.employee_gender,e.employee_password " +
                "FROM employee e " +
                "LEFT JOIN employee_phone p ON e.employee_id = p.employee_id";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            Map<Integer, Employee> employeeMap = new HashMap<>();
            while (rs.next()) {
                int employee_id = rs.getInt("employee_id");
                Employee employee = employeeMap.get(employee_id);
                if (employee == null) {
                    employee = new Employee(
                            employee_id,
                            rs.getString("employee_name"),
                            rs.getString("employee_email"),
                            rs.getString("phone_number"),
                            rs.getString("employee_position"),
                            rs.getString("employee_address"),
                            rs.getDouble("salary"),
                            rs.getString("employee_gender"),
                            rs.getString("employee_password")
                    );

                    employeeMap.put(employee_id, employee);
                    employees.add(employee);
                } else {
                    String existingPhones = employee.getPhone();
                    employee.setPhone(existingPhones + ", " + rs.getString("phone_number"));
                }
            }
            employeeData.setAll(employees);
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load employees: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void sellProductAction() {
        if (productComboBox.getSelectionModel().isEmpty() || tableView.getSelectionModel().isEmpty()) {
            showAlert("Warning", "Please select both a product and a customer.", Alert.AlertType.WARNING);
            return;
        }

        Product selectedProduct = productComboBox.getSelectionModel().getSelectedItem();
        Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
        Employee selectedEmployee = employeeComboBox.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null || selectedProduct == null) {
            showAlert("Warning", "Please ensure both a product and an employee are selected.", Alert.AlertType.WARNING);
            return;
        }

        if (selectedProduct.getQuantity() == 0) {
            showAlert("Warning", "Product is out of stock.", Alert.AlertType.WARNING);
            return;
        }

        try {
            processSale(selectedProduct, selectedCustomer, selectedEmployee);
            loadProducts(); // Refresh products list after sale
            showAlert("Success", "Sold " + selectedProduct.getProductName() + " to " + selectedCustomer.getName(), Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to execute sales transaction: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void processSale(Product product, Customer customer, Employee employee) throws SQLException {
        Connection conn = DatabaseConnection.connect();
        try {
            conn.setAutoCommit(false);

            String saleQuery = "INSERT INTO sales (sales_date, sales_amount, customer_id, employee_id, product_id, payment_method) VALUES (CURRENT_DATE, ?, ?, ?, ?, 'Cash')";
            try (PreparedStatement pstmt = conn.prepareStatement(saleQuery)) {
                pstmt.setInt(1, (int) product.getCost());
                pstmt.setInt(2, customer.getCustomerId());
                pstmt.setInt(3, employee.getEmployeeId());
                pstmt.setInt(4, product.getProductId());
                pstmt.executeUpdate();
            }

            String linkQuery = "INSERT INTO customer_product (customer_id, product_id) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(linkQuery)) {
                pstmt.setInt(1, customer.getCustomerId());
                pstmt.setInt(2, product.getProductId());
                pstmt.executeUpdate();
            }

            updateProductStock(conn, product, product.getQuantity() - 1);

            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }


    public ObservableList<Product> getCustomerPurchases(int customerId) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = """
                SELECT p.product_id, p.product_name, p.cost, p.quantity,p.storage_id
                FROM customer_product cp
                JOIN product p ON cp.product_id = p.product_id
                WHERE cp.customer_id = ?;
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getString("product_name"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("cost"),
                        rs.getInt("storage_id") // Ensure storage_id is available or handle it accordingly
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customer purchases: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return products;
    }


    private void updateProductStock(Connection conn, Product product, int newQuantity) throws SQLException {
        String updateQuery = "UPDATE product SET quantity = ? WHERE product_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, product.getProductId());
            pstmt.executeUpdate();
        }
    }


    private void showPurchasedProductsDialog(Customer customer) {
        ObservableList<Product> purchasedProducts = getCustomerPurchases(customer.getCustomerId());
        if (purchasedProducts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No purchased products found for " + customer.getName());
            alert.showAndWait();
            return;
        }
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(customer.getName() + "'s Purchased Products");

        TableView<Product> productsTable = new TableView<>();
        TableColumn<Product, String> productNameColumn = new TableColumn<>("Product Name");
        TableColumn<Product, Double> productPriceColumn = new TableColumn<>("Price");

        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        productNameColumn.prefWidthProperty().bind(productsTable.widthProperty().multiply(0.6));
        productPriceColumn.prefWidthProperty().bind(productsTable.widthProperty().multiply(0.4));

        productsTable.getColumns().addAll(productNameColumn, productPriceColumn);
        productsTable.setItems(purchasedProducts);

        VBox vbox = new VBox(10, productsTable);
        vbox.setPadding(new Insets(15));

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    @FXML
    public void onCustomerDoubleClick(MouseEvent event) {

        if (event.getClickCount() == 2 && tableView.getSelectionModel().getSelectedItem() != null) {
            showPurchasedProductsDialog(tableView.getSelectionModel().getSelectedItem());
        }
    }

}
