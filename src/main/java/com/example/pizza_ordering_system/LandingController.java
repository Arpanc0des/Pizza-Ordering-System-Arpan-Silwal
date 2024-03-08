package com.example.pizza_ordering_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
public class LandingController implements Initializable {

    private Stage primaryStage;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public TextField nameField;
    public TextField phoneNumField;
    public ComboBox comboBoxId;
    public TextField toppingsField;
    public TextField NameCRUDField;
    public TextField pizzaNumberCRUDField;
    public ComboBox comboBoxCRUDField;
    public TextField ToppingCRUDField;
    public Label errorMessage;
    public TableView<PizzaORM> pizzaTable_view;
    public TableColumn<PizzaORM, Integer> pidTView;
    public TableColumn<PizzaORM, String> nameTView;
    public TableColumn<PizzaORM, String> sizeTView;
    public TableColumn<PizzaORM, Integer> toppingsTView;
    public ObservableList<PizzaORM> list = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pidTView.setCellValueFactory(new PropertyValueFactory<>("pidORM"));
        nameTView.setCellValueFactory(new PropertyValueFactory<>("nameORM"));
        sizeTView.setCellValueFactory(new PropertyValueFactory<>("pizzaSizeORM"));
        toppingsTView.setCellValueFactory(new PropertyValueFactory<>("toppingsORM"));
        pizzaTable_view.setItems(list);
        comboBoxId.setItems(FXCollections.observableArrayList("S", "M", "L", "XL"));
        fetchTable();
    }
    public void fetchTable() {
        list.clear(); // Clear the ObservableList before adding new items
        // Establish a database connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/pizzadb";
        String dbUser = "root";
        String dbPassword = "";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            // Execute a SQL query to retrieve data from the database
            String query = "SELECT * FROM `order_tbl`";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            // Populate the ObservableList with data from the database
            while (resultSet.next()) {
                int id = resultSet.getInt("pizza_id");
                String name = resultSet.getString("name");
                int phone = resultSet.getInt("phone_number");
                String size = resultSet.getString("pizza_size");
                int toppings = resultSet.getInt("no_of_toppings");
                list.add(new PizzaORM(id, name, phone, size, toppings));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void insertPizza() {
        if (nameField.getText().isEmpty() || phoneNumField.getText().isEmpty() || comboBoxId.getValue() == null || toppingsField.getText().isEmpty()) {
            errorMessage.setText("Please fill in all required fields.");
            return;
        }
        try {
            Integer.parseInt(phoneNumField.getText());
        } catch (NumberFormatException e) {
            errorMessage.setText("Please enter a valid phone number.");
            return;
        }
        try {
            Integer.parseInt(toppingsField.getText());
        } catch (NumberFormatException e) {
            errorMessage.setText("Please enter a valid number of toppings.");
            return;
        }
        double totalBill = calculateTotalBill();

        String jdbcUrl = "jdbc:mysql://localhost:3306/pizzadb";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO order_tbl (name, phone_number, pizza_size, no_of_toppings, total_bill) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setInt(2, Integer.parseInt(phoneNumField.getText()));
            preparedStatement.setString(3, comboBoxId.getValue().toString());
            preparedStatement.setInt(4, Integer.parseInt(toppingsField.getText()));
            preparedStatement.setDouble(5, totalBill);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                fetchTable();
            } else {
                errorMessage.setText("Error: Failed to insert pizza order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage.setText("Error: SQL Exception occurred.");
        }
    }
    public double calculateTotalBill() {
        String pizzaSize = comboBoxId.getValue().toString();
        int toppings = Integer.parseInt(toppingsField.getText());
        double basePrice = 0.0;
        switch (pizzaSize) {
            case "XL":
                basePrice = 15.00;
                break;
            case "L":
                basePrice = 12.00;
                break;
            case "M":
                basePrice = 10.00;
                break;
            case "S":
                basePrice = 8.00;
                break;
            default:
                errorMessage.setText("Invalid pizza size selected.");
                return 0.0;
        }
        double totalPriceBeforeTax = basePrice + (toppings * 1.50); // topping costs $1.50
        double totalBill = totalPriceBeforeTax * 1.15; // adding 15% HST
        return Math.round(totalBill);
    }
    public void updatePizza() {
        if (NameCRUDField.getText().isEmpty() || pizzaNumberCRUDField.getText().isEmpty() || comboBoxCRUDField.getValue() == null || ToppingCRUDField.getText().isEmpty()) {
            errorMessage.setText("Error: Please fill in all fields to update the pizza order.");
            return;
        }
        try {
            int pizzaNumber = Integer.parseInt(pizzaNumberCRUDField.getText());
            double totalBill = calculateTotalBill();

            String jdbcUrl = "jdbc:mysql://localhost:3306/pizzadb";
            String dbUser = "root";
            String dbPassword = "";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String query = "UPDATE `order_tbl` SET name = ?, phone_number = ?, pizza_size = ?, no_of_toppings = ?, total_bill = ? WHERE pizza_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, NameCRUDField.getText());
                preparedStatement.setInt(2, Integer.parseInt(phoneNumField.getText()));
                preparedStatement.setString(3, comboBoxCRUDField.getValue().toString());
                preparedStatement.setInt(4, Integer.parseInt(ToppingCRUDField.getText()));
                preparedStatement.setDouble(5, totalBill);
                preparedStatement.setInt(6, pizzaNumber);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    fetchTable();
                } else {
                    errorMessage.setText("Error: Pizza order with ID " + pizzaNumber + " does not exist.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                errorMessage.setText("Error: SQL Exception occurred.");
            }
        } catch (NumberFormatException e) {
            errorMessage.setText("Error: Please enter a valid pizza number.");
        }
    }
    public void deletePizza() {
        // Check if the pizza number field is empty
        if (pizzaNumberCRUDField.getText().isEmpty()) {
            errorMessage.setText("Error: Please enter the pizza number to delete.");
            return;
        }

        try {
            // Get the pizza number to delete
            int pizzaNumber = Integer.parseInt(pizzaNumberCRUDField.getText());

            // Establish a database connection
            String jdbcUrl = "jdbc:mysql://localhost:3306/pizzadb";
            String dbUser = "root";
            String dbPassword = "";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                // Execute a SQL query to delete the pizza order
                String query = "DELETE FROM `order_tbl` WHERE pizza_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, pizzaNumber);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    // If deletion is successful, fetch and display updated table
                    fetchTable();
                } else {
                    errorMessage.setText("Error: Pizza order with ID " + pizzaNumber + " does not exist.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                errorMessage.setText("Error: SQL Exception occurred.");
            }
        } catch (NumberFormatException e) {
            errorMessage.setText("Error: Please enter a valid pizza number.");
        }
    }
    public void checkoutPizza() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("checkout-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage checkoutStage = new Stage();
            checkoutStage.setScene(scene);
            Stage currentStage = (Stage) nameField.getScene().getWindow();
            currentStage.close();
            checkoutStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}