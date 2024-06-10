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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class StorageController {
    @FXML
    private TableView<Storage> tableView;
    @FXML
    private TableColumn<Storage, Integer> storageId;
    @FXML
    private TableColumn<Storage, String> storageName;
    @FXML
    private TableColumn<Storage, Integer> storageCapacity;
    @FXML
    private TableColumn<Storage, String> storageLocation;

    @FXML
    private TextField tfId, tfName, tfCapacity, tfLocation;

    private ObservableList<Storage> storageList = FXCollections.observableArrayList();
    private static ObservableList<Items> availableItems = FXCollections.observableArrayList();
    private ComboBox<Items> comboBox = new ComboBox<>();
    private TableView<Items> itemsTable;


    @FXML
    public void initialize() throws SQLException {
        setupTableColumns();
        tableView.setItems(storageList);
        loadStorageData();

        // Setup ComboBox to display item names
        setupItemsComboBox();
    }

    private void setupItemsComboBox() {
        comboBox.setCellFactory(lv -> new ListCell<Items>() {
            @Override
            protected void updateItem(Items item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getProductName()); // Assuming Items class has a getProductName method
            }
        });

        comboBox.setButtonCell(new ListCell<Items>() {
            @Override
            protected void updateItem(Items item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getProductName());
            }
        });

        comboBox.setItems(getAvailableItems()); // Ensure this method returns ObservableList<Items>
    }

    private void setupTableColumns() {
        storageId.setCellValueFactory(new PropertyValueFactory<>("storageId"));
        storageName.setCellValueFactory(new PropertyValueFactory<>("storageName"));
        storageCapacity.setCellValueFactory(new PropertyValueFactory<>("storageCapacity"));
        storageLocation.setCellValueFactory(new PropertyValueFactory<>("storageLocation"));
    }


    @FXML
    private void addStorage(ActionEvent event) throws SQLException {
        if (tfId.getText().isEmpty() || tfName.getText().isEmpty() || tfCapacity.getText().isEmpty() || tfLocation.getText().isEmpty()) {
            showAlert("Error", "Please fill in all fields.", Alert.AlertType.ERROR);
            return;
        }
        if (Integer.parseInt(tfId.getText()) == 0) {
            showAlert("Error", "Storage ID cannot be 0.", Alert.AlertType.ERROR);
            return;
        }
        if (storageList.stream().anyMatch(storage -> storage.getStorageId() == Integer.parseInt(tfId.getText()))) {
            showAlert("Error", "Storage ID already exists.", Alert.AlertType.ERROR);
            return;
        }

        if (storageList.stream().anyMatch(storage -> storage.getStorageName().equals(tfName.getText()))) {
            showAlert("Error", "Storage name already exists.", Alert.AlertType.ERROR);
            return;
        }

        if (Integer.parseInt(tfCapacity.getText()) == 0) {
            showAlert("Error", "Storage capacity cannot be 0.", Alert.AlertType.ERROR);
            return;
        }
        String sql = "INSERT INTO Storage (storage_id, storage_name, storage_capacity, storage_location) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            showAlert("Database Error", "Failed to connect to database.", Alert.AlertType.ERROR);
            return;
        } else {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(tfId.getText()));
            pstmt.setString(2, tfName.getText());
            pstmt.setInt(3, Integer.parseInt(tfCapacity.getText()));
            pstmt.setString(4, tfLocation.getText());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                storageList.add(new Storage(tfName.getText(), Integer.parseInt(tfId.getText()),
                        Integer.parseInt(tfCapacity.getText()), tfLocation.getText()));
                clearFields();
                showAlert("Success", "Storage entry added.", Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    private void updateStorage(ActionEvent event) throws SQLException {
        if (tfId.getText().isEmpty() || tfName.getText().isEmpty() || tfCapacity.getText().isEmpty() || tfLocation.getText().isEmpty()) {
            showAlert("Error", "Please fill in all fields.", Alert.AlertType.ERROR);
            return;
        }
        if (Integer.parseInt(tfId.getText()) == 0) {
            showAlert("Error", "Storage ID cannot be 0.", Alert.AlertType.ERROR);
            return;
        }

        if (Integer.parseInt(tfCapacity.getText()) == 0) {
            showAlert("Error", "Storage capacity cannot be 0.", Alert.AlertType.ERROR);
            return;
        }
        if (storageList.stream().anyMatch(storage -> storage.getStorageName().equals(tfName.getText()))) {
            showAlert("Error", "Storage name already exists.", Alert.AlertType.ERROR);
            return;
        }
        if (storageList.stream().anyMatch(storage -> storage.getStorageId() == Integer.parseInt(tfId.getText()))) {
            showAlert("Error", "Storage ID already exists.", Alert.AlertType.ERROR);
            return;
        }
        String sql = "UPDATE Storage SET storage_name = ?, storage_capacity = ?, storage_location = ? WHERE storage_id = ?";
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            showAlert("Database Error", "Failed to connect to database.", Alert.AlertType.ERROR);
            return;
        } else {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tfName.getText());
            pstmt.setInt(2, Integer.parseInt(tfCapacity.getText()));
            pstmt.setString(3, tfLocation.getText());
            pstmt.setInt(4, Integer.parseInt(tfId.getText()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                Storage selected = tableView.getSelectionModel().getSelectedItem();
                selected.setStorageName(tfName.getText());
                selected.setStorageCapacity(Integer.parseInt(tfCapacity.getText()));
                selected.setStorageLocation(tfLocation.getText());
                tableView.refresh();
                clearFields();
                showAlert("Success", "Storage entry updated.", Alert.AlertType.INFORMATION);
            }

        }
    }

    @FXML
    private void deleteStorage(ActionEvent event) throws SQLException {
        if (tfId.getText().isEmpty()) {
            showAlert("Error", "Please select a storage entry to delete.", Alert.AlertType.ERROR);
            return;
        }
        String sql = "DELETE FROM Storage WHERE storage_id = ?";
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            showAlert("Database Error", "Failed to connect to database.", Alert.AlertType.ERROR);
            return;
        } else {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(tfId.getText()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                Storage selected = tableView.getSelectionModel().getSelectedItem();
                storageList.remove(selected);
                clearFields();
                showAlert("Success", "Storage entry deleted.", Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    public void rowClicked(MouseEvent event) {
        Storage selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tfId.setText(String.valueOf(selected.getStorageId()));
            tfName.setText(selected.getStorageName());
            tfCapacity.setText(String.valueOf(selected.getStorageCapacity()));
            tfLocation.setText(selected.getStorageLocation());
        }
    }

    private void clearFields() {
        tfId.clear();
        tfName.clear();
        tfCapacity.clear();
        tfLocation.clear();
    }

    @FXML
    private void handleSelectButton(ActionEvent event) {
        Storage selectedStorage = tableView.getSelectionModel().getSelectedItem();
        if (selectedStorage != null) {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Manage Items in " + selectedStorage.getStorageName());

            itemsTable = new TableView<>();
            setupItemsTable(itemsTable);
            itemsTable.setItems(selectedStorage.getItemsList());

            comboBox.setPromptText("Select an item to add");

            Button addButton = new Button("Add Item");
            addButton.setOnAction(e -> {
                Items selectedItem = comboBox.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    selectedStorage.getItemsList().add(selectedItem);
                    itemsTable.refresh();
                }
            });

            VBox vbox = new VBox(10, itemsTable, comboBox, addButton);
            dialog.getDialogPane().setContent(vbox);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
        } else {
            showAlert("Error", "No storage selected. Please select a storage entry first.", Alert.AlertType.ERROR);
        }
    }

    private void loadStorageData() {
        try {
            Connection conn = DatabaseConnection.connect();
            String sql = "SELECT * FROM Storage";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Storage storage = new Storage(
                        rs.getString("storage_name"),
                        rs.getInt("storage_id"),
                        rs.getInt("storage_capacity"),
                        rs.getString("storage_location")
                );
                storage.getItemsList().addAll(loadItemsForStorage(storage.getStorageId()));
                storageList.add(storage);
            }
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load storage data: " + ex.getMessage(), Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }


    private ObservableList<Items> loadItemsForStorage(int storageId) throws SQLException {
        ObservableList<Items> items = FXCollections.observableArrayList();
        String itemSQL = "SELECT * FROM product WHERE storage_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(itemSQL)) {
            pstmt.setInt(1, storageId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new Items(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getDouble("cost"),
                            rs.getInt("quantity"),
                            rs.getInt("storage_id")
                    ));
                }
            }
        }
        return items;
    }


    public static ObservableList<Items> getAvailableItems() {
        try {
            String sql = "SELECT product_id, product_name, cost, quantity, storage_id FROM Product";
            Connection conn = DatabaseConnection.connect();
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    availableItems.add(new Items(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getDouble("cost"),
                            rs.getInt("quantity"),
                            rs.getInt("storage_id")
                    ));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return availableItems;
    }

    private void setupItemsTable(TableView<Items> itemsTable) {
        TableColumn<Items, Integer> itemIdColumn = new TableColumn<>("Product ID");
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        TableColumn<Items, String> itemNameColumn = new TableColumn<>("product Name");
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        TableColumn<Items, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<Items, Double> costColumn = new TableColumn<>("Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        itemsTable.getColumns().addAll(itemIdColumn, itemNameColumn, quantityColumn, costColumn);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
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
