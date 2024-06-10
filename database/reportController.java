package com.example.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class reportController implements Initializable {

    @FXML
    private Label reportTitle;
    @FXML
    private TextField tfTopEmployee, tfTopCustomer, tfMostPurchasedProduct;

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> employeeIdColumn;
    @FXML
    private TableColumn<Employee, String> employeeNameColumn;
    @FXML
    private TableColumn<Employee, Double> employeeSalaryColumn;
    @FXML
    private TextField numEmployees;
    @FXML
    private TextField totalSalaries;
    @FXML
    private TableView<Product> itemTable;
    @FXML
    private TableColumn<Product, Integer> itemIdColumn;
    @FXML
    private TableColumn<Product, String> itemTypeColumn;
    @FXML
    private TableColumn<Product, Double> itemCostColumn;
    @FXML
    private TextField numItems;
    @FXML
    private TextField totalCostItems;

    @FXML
    private TableView<Supplier> supplierTable;
    @FXML
    private TableColumn<Supplier, Integer> supplierIdColumn;
    @FXML
    private TableColumn<Supplier, String> supplierNameColumn;
    @FXML
    private TableColumn<Supplier, String> supplierAddressColumn;
    @FXML
    private TextField numSuppliers;

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerAddressColumn;
    @FXML
    private TextField numCustomers;

    @FXML
    private Label profitsLabel;
    @FXML
    private TextField totalIncomes;
    @FXML
    private TextField totalOutcomes;
    @FXML
    private TextField profit;

    @FXML
    private TableView<Equipment> equipmentTable;
    @FXML
    private TableColumn<Equipment, Integer> equipmentIdColumn;
    @FXML
    private TableColumn<Equipment, String> equipmentNameColumn;
    @FXML
    private TableColumn<Equipment, Double> equipmentCostColumn;
    @FXML
    private TextField numEquipments;
    ObservableList<Employee> employeeData = FXCollections.observableArrayList();
    @FXML
    private TextField totalCostEquipments;
    private ObservableList<Supplier> supplierData = FXCollections.observableArrayList();
    @FXML
    private Button btBack;
    private ObservableList<Product> productData = FXCollections.observableArrayList();
    private TextField topCustomerLabel;
    private Label topEmployeeLabel;

    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("report.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Report Application");
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void main(Stage primaryStage) {
        start(primaryStage);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeColumns();
        loadEmployees();
        loadProducts();
        loadSupplierData();
        loadCustomerData();
        loadEquipmentData();
        loadFinancials();
        displayTopCustomer();
        displayTopSellingEmployee();
        loadMostPurchasedProduct();
    }

    private void initializeColumns() {
        employeeIdColumn.setCellValueFactory(cellData -> cellData.getValue().employeeIdProperty().asObject());
        employeeNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        employeeSalaryColumn.setCellValueFactory(cellData -> cellData.getValue().salaryProperty().asObject());

        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        itemCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        supplierIdColumn.setCellValueFactory(cellData -> cellData.getValue().supplierIdProperty().asObject());
        supplierNameColumn.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        supplierAddressColumn.setCellValueFactory(cellData -> cellData.getValue().supplierAddressProperty());

        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        customerAddressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        equipmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        equipmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        equipmentCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

    private void loadEmployees() {
        String query = "SELECT e.*, ep.phone_number FROM employee e LEFT JOIN employee_phone ep ON e.employee_id = ep.employee_id";
        String totalsQuery = "SELECT COUNT(*) AS TotalEmployees, SUM(salary) AS TotalSalary FROM employee";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();
             PreparedStatement totalStmt = conn.prepareStatement(totalsQuery);
             ResultSet totalRs = totalStmt.executeQuery()) {

            ObservableList<Employee> employees = FXCollections.observableArrayList();
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("employee_email"),
                        rs.getString("phone_number"),
                        rs.getString("employee_position"),
                        rs.getString("employee_address"),
                        rs.getDouble("salary"),
                        rs.getString("employee_gender"),
                        rs.getString("employee_password")
                ));
            }
            employeeTable.setItems(employees);

            if (totalRs.next()) {
                numEmployees.setText(String.valueOf(totalRs.getInt("TotalEmployees")));
                totalSalaries.setText(String.format("%.2f", totalRs.getDouble("TotalSalary")));
            }
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load employees: " + ex.getMessage(), Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }


    private void loadProducts() {
        String queryDetails = "SELECT product_id, product_name, quantity, cost, storage_id FROM product";
        String queryAggregates = "SELECT COUNT(product_id) as TotalItems, SUM(cost * quantity) as TotalCost FROM product";

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmtDetails = conn.prepareStatement(queryDetails); ResultSet rsDetails = stmtDetails.executeQuery(); PreparedStatement stmtAggregates = conn.prepareStatement(queryAggregates); ResultSet rsAggregates = stmtAggregates.executeQuery()) {

            productData.clear();
            int itemCount = 0;
            double totalCost = 0;

            while (rsDetails.next()) {
                Product product = new Product(rsDetails.getString("product_name"), rsDetails.getInt("product_id"), rsDetails.getInt("quantity"), rsDetails.getDouble("cost"), rsDetails.getInt("storage_id"));
                productData.add(product);
            }

            if (rsAggregates.next()) {
                itemCount = rsAggregates.getInt("TotalItems");
                totalCost = rsAggregates.getDouble("TotalCost");
            }

            itemTable.setItems(productData);
            numItems.setText(Integer.toString(itemCount));
            totalCostItems.setText(String.format("%.2f", totalCost));

        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load products: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void loadSupplierData() {
        ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
        String query = "SELECT s.supplier_id, s.supplier_name, s.supplier_address, s.supplier_email, sp.phone_number FROM supplier s LEFT JOIN supplier_phone sp ON s.supplier_id = sp.supplier_id";
        String countQuery = "SELECT COUNT(*) AS TotalSuppliers FROM supplier"; // Simple count query

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery(); PreparedStatement countStmt = conn.prepareStatement(countQuery); // Prepare and execute the count query separately
             ResultSet countRs = countStmt.executeQuery()) {

            int totalSuppliers = 0;
            if (countRs.next()) {
                totalSuppliers = countRs.getInt("TotalSuppliers");
            }
            numSuppliers.setText(String.valueOf(totalSuppliers)); // Set the count of suppliers

            while (rs.next()) {
                Supplier supplier = new Supplier(rs.getInt("supplier_id"), rs.getString("supplier_name"), rs.getString("supplier_address"), rs.getInt("phone_number"), rs.getString("supplier_email"));
                supplierList.add(supplier);
            }
            supplierData.setAll(supplierList);
            supplierTable.setItems(supplierData);

        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load suppliers: " + ex.getMessage(), Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadCustomerData() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        String query = "SELECT customer.customer_id, customer.customer_name, customer.customer_address, customer.customer_email, cs.phone_number FROM customer" + " LEFT JOIN customer_phone cs ON customer.customer_id = cs.customer_id";
        try (Connection conn = DatabaseConnection.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("customer_id"), rs.getString("customer_name"), rs.getString("customer_address"), rs.getString("customer_email"), rs.getInt("phone_number"));
                customerList.add(customer);
            }
            customerTable.setItems(customerList);
            numCustomers.setText(String.valueOf(customerList.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEquipmentData() {
        ObservableList<Equipment> equipmentList = FXCollections.observableArrayList();
        String query = "SELECT equipment_id,equipment_name,quantity,cost,storage_id FROM equipment";
        try (Connection conn = DatabaseConnection.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Equipment equipment = new Equipment(rs.getInt("equipment_id"), rs.getString("equipment_name"), rs.getInt("quantity"), rs.getDouble("cost"), rs.getInt("storage_id"));
                equipmentList.add(equipment);
            }
            equipmentTable.setItems(equipmentList);

            numEquipments.setText(String.valueOf(equipmentList.size()));
            totalCostEquipments.setText(String.valueOf(equipmentList.stream().mapToDouble(Equipment::getCost).sum()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("centerScreen.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Center Screen");
            stage.show();
            Stage currentStage = (Stage) btBack.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while opening the next screen.", Alert.AlertType.ERROR);
        }
    }

    private void loadFinancials() {
        String incomeQuery = "SELECT SUM(sales_amount) AS TotalIncome FROM sales";
        String productCostQuery = "SELECT SUM(p.cost * p.quantity) AS ProductCost FROM product p";
        String equipmentCostQuery = "SELECT SUM(e.cost * e.quantity) AS EquipmentCost FROM equipment e";
        String salaryCostQuery = "SELECT SUM(es.salary) AS TotalSalary FROM employee es";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement incomeStmt = conn.prepareStatement(incomeQuery);
             ResultSet incomeRs = incomeStmt.executeQuery();
             PreparedStatement productCostStmt = conn.prepareStatement(productCostQuery);
             ResultSet productCostRs = productCostStmt.executeQuery();
             PreparedStatement equipmentCostStmt = conn.prepareStatement(equipmentCostQuery);
             ResultSet equipmentCostRs = equipmentCostStmt.executeQuery();
             PreparedStatement salaryCostStmt = conn.prepareStatement(salaryCostQuery);
             ResultSet salaryCostRs = salaryCostStmt.executeQuery()) {

            double totalIncome = 0;
            double totalOutcome = 0;

            if (incomeRs.next()) {
                totalIncome = incomeRs.getDouble("TotalIncome");
                totalIncomes.setText(String.format("%.2f", totalIncome));
            }

            double productCost = 0;
            double equipmentCost = 0;
            double totalSalary = 0;

            if (productCostRs.next()) {
                productCost = productCostRs.getDouble("ProductCost");
            }

            if (equipmentCostRs.next()) {
                equipmentCost = equipmentCostRs.getDouble("EquipmentCost");
            }

            if (salaryCostRs.next()) {
                totalSalary = salaryCostRs.getDouble("TotalSalary");
            }

            totalOutcome = productCost + equipmentCost + totalSalary;
            totalOutcomes.setText(String.format("%.2f", totalOutcome));

            // For debugging: check each part of the outcome calculation
            System.out.println("Product Cost: " + productCost);
            System.out.println("Equipment Cost: " + equipmentCost);
            System.out.println("Total Salary: " + totalSalary);

            profit.setText(String.format("%.2f", totalIncome - totalOutcome));

        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load financial data: " + ex.getMessage(), Alert.AlertType.ERROR);
            ex.printStackTrace();
}
    }

    @FXML
    private void displayTopCustomer() {
        String query = """
                    SELECT c.customer_name, COUNT(cp.product_id) AS total_purchases
                    FROM customer c
                    JOIN customer_product cp ON c.customer_id = cp.customer_id
                    GROUP BY c.customer_id
                    ORDER BY total_purchases DESC
                    LIMIT 1;
                """;

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String topCustomer = rs.getString("customer_name");
                int purchases = rs.getInt("total_purchases");
                tfTopCustomer.setText("Top Customer: " + topCustomer + " (Purchases: " + purchases + ")");
            } else {
                tfTopCustomer.setText("Top Customer: Not available");
            }

        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to fetch top customer: " + ex.getMessage(), Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }

    @FXML
    private void displayTopSellingEmployee() {
        String query = """
                    SELECT e.employee_name, COUNT(s.product_id) AS total_sales
                    FROM employee e
                    JOIN sales s ON e.employee_id = s.employee_id
                    GROUP BY e.employee_id
                    ORDER BY total_sales DESC
                    LIMIT 1;
                """;

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String topEmployee = rs.getString("employee_name");
                int sales = rs.getInt("total_sales");
                tfTopEmployee.setText("Top Selling Employee: " + topEmployee + " (Sales: " + sales + ")");
            } else {
                tfTopEmployee.setText("Top Selling Employee: Not available");
            }

        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to fetch top selling employee: " + ex.getMessage(), Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }

    @FXML
    private void loadMostPurchasedProduct() {
        String query = """
                    SELECT p.product_name, COUNT(s.product_id) AS total_sales
                    FROM product p
                    JOIN sales s ON p.product_id = s.product_id
                    GROUP BY p.product_id
                    ORDER BY total_sales DESC
                    LIMIT 1;
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String productName = rs.getString("product_name");
                int salesCount = rs.getInt("total_sales");
                tfMostPurchasedProduct.setText("Most Purchased Product: " + productName + " (Sold " + salesCount + " times)");
            } else {
                tfMostPurchasedProduct.setText("No sales data available.");
            }

        } catch (SQLException ex) {
            tfMostPurchasedProduct.setText("Failed to load data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Add other methods and initialization code as necessary
}


