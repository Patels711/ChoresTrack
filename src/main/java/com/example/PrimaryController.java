package com.example;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrimaryController {

    @FXML
    private Text welcomeText;
    @FXML
    private Text transactions;
    @FXML
    private Text choresincome;
    @FXML
    private Text activityprice;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ChoiceBox<String> activityChoice;

    @FXML
    private Text userName;

    private App app;
    private String currentUsername; // To store the logged-in username
    private Connection connection; // To hold the passed database connection

    UserManager dataValidation = new UserManager("src/main/java/com/example/data.txt");

    public String chore;
    public String activity;

    // Method to inject the App instance
    public void setApp(App app) {
        this.app = app;
    }

    // New method to set the database connection
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    // Method to set the username
    public void setUsername(String username) {
        this.currentUsername = username; // Store the username
        if (welcomeText != null) {
            // Fetch balance from DB and display it
            double balance = getUserBalance(username);
            welcomeText.setText("Hello " + username + "…\nAccount:\nName: " + username + "\nTotal Balance: $" + String.format("%.2f", balance));
        }
        if (userName != null) {
            userName.setText(username);
        }
    }
    
    // Method to get a user's balance
    private double getUserBalance(String username) {
        String sql = "SELECT balance FROM users WHERE username = ?";
        double balance = 0.0;

        if (this.connection == null) {
            System.err.println("Error: Database connection is null in getUserBalance.");
            return 0.0;
        }
        try {
            if (this.connection.isClosed()) {
                System.err.println("Error: Database connection is closed in getUserBalance.");
                return 0.0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status in getUserBalance: " + e.getMessage());
            return 0.0;
        }

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                balance = rs.getDouble("balance");
            } else {
                // If user doesn't exist, insert them with a default balance
                insertUser(username, 0.00);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user balance: " + e.getMessage());
        }
        return balance;
    }

    // Method to insert a new user with a default balance
    private void insertUser(String username, double initialBalance) {
        String sql = "INSERT INTO users(username, balance) VALUES(?,?)";
        if (this.connection == null) {
            System.err.println("Error: Database connection is null in insertUser.");
            return;
        }
        try {
            if (this.connection.isClosed()) {
                System.err.println("Error: Database connection is closed in insertUser.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status in insertUser: " + e.getMessage());
            return;
        }

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setDouble(2, initialBalance);
            pstmt.executeUpdate();
            System.out.println("User " + username + " inserted with balance " + initialBalance);
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
        }
    }

    // Method to update a user's balance
    private void updateUserBalance(String username, double amount, String message) {
        String sql = "UPDATE users SET balance = balance + ? WHERE username = ?";
        if (this.connection == null) {
            System.err.println("Error: Database connection is null in updateUserBalance.");
            return;
        }
        try {
            if (this.connection.isClosed()) {
                System.err.println("Error: Database connection is closed in updateUserBalance.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status in updateUserBalance: " + e.getMessage());
            return;
        }

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            System.out.println("Balance updated for " + username + " by " + amount);
            System.out.println(message);
        } catch (SQLException e) {
            System.err.println("Error updating user balance: " + e.getMessage());
        }
        // Update the displayed balance immediately after a transaction
        if (welcomeText != null && currentUsername != null) {
            setUsername(currentUsername); // Re-fetch and display updated balance
        }
    }

    // Method to insert a new message
    private void insertMessage(String username, String message) {
        String sql = "INSERT INTO messages(username, message, timestamp) VALUES(?,?,CURRENT_TIMESTAMP)";
        if (this.connection == null) {
            System.err.println("Error: Database connection is null in insertMessage.");
            return;
        }
        try {
            if (this.connection.isClosed()) {
                System.err.println("Error: Database connection is closed in insertMessage.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status in insertMessage: " + e.getMessage());
            return;
        }

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, message);
            pstmt.executeUpdate();
            System.out.println("Message '" + message + "' inserted for user " + username);
        } catch (SQLException e) {
            System.err.println("Error inserting message: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // ... (existing initialization code)
        // Initialize ChoiceBox for chores
        ObservableList<String> choreList = FXCollections.observableArrayList(
            "", "Make your bed (daily)", "Clean up your room", "Do the dishes/load dishwasher", "Unload dishwasher", "Take out the trash", "Vacuum one room", "Vacuum the whole house", "Sweep and mop floors", "Clean the bathroom", "Fold laundry", "Do a full load of laundry (wash + dry + fold)", "Water indoor plants", "Feed pets", "Walk the dog (15–30 mins)", "Yard work (pull weeds, rake, etc.)", "Mow the lawn", "Wash the car", "Clean windows/mirrors", "Organize a closet or drawer", "Help cook dinner", "Babysit younger sibling (1 hr)", "Clean garage or basement (supervised)"
        );
        choiceBox.setItems(choreList);
        // Map chores to values (example: monetary values)
        double[] choreValues = {0.00, 0.25, 1.00, 1.00, 0.75, 0.50, 0.75, 2.00, 2.00, 3.00, 1.00, 2.50, 0.50, 0.50, 1.50, 3.00, 5.00, 4.00, 1.50, 2.00, 2.00, 5.00, 4.00};

        choiceBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if(showInputDialog("Password", "Enter Password to Proceed", "Enter Password to validate transactions", "Enter Password")){
                        if (newValue != null) {
                            chore = "Selected Chore: " + newValue;
                            // Find the index of the selected chore
                            int num = choreList.indexOf(newValue);
                            if (num != -1 && num < choreValues.length) {
                                double val = choreValues[num];
                                choresincome.setVisible(true);
                                choresincome.setText("+$" + String.format("%.2f", val)); // Display income with sign
                                // Update user's balance for chore completion
                                if (currentUsername != null) {
                                    updateUserBalance(currentUsername, val, chore); // Add value to balance
                                    // Insert the message into the database
                                    insertMessage(currentUsername, "Completed chore: " + newValue + " (+$" + String.format("%.2f", val) + ")");
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Initialize ChoiceBox for activities
        ObservableList<String> activityList = FXCollections.observableArrayList(
            "", "15 minutes of screen time", "30 minutes of screen time", "Choose the family movie or show", "Extra 30 minutes before bedtime", "Pick what’s for dinner", "Small treat or snack (candy, chips)", "Ice cream outing", "Baking with a parent", "$5 gift card", "$10 gift card", "Download a new app or game", "Trip to the park, pool, or fun center", "Invite a friend over", "Go to a movie theater", "One “no chore” pass", "Choose music on car rides for the day", "Craft/art session with supplies", "30 minutes of LEGO/building time", "Hobby/project time (drawing, music, etc.)", "1-hour gaming session"
        );
        activityChoice.setItems(activityList);
        double[] price = {0.00, 0.50, 1.00, 1.50, 1.50, 2.00, 1.00, 3.00, 2.00, 5.00, 10.00, 6.00, 5.00, 2.50, 6.00, 4.00, 1.00, 2.50, 1.00, 2.00, 2.00};

        activityChoice.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if(showInputDialog("Password", "Enter Password to Proceed", "Enter Password to validate transactions", "Enter Password")){
                        if(getUserBalance(currentUsername)<=0.00){
                            insertMessage(currentUsername, "Balance is zero, perform chore to get money");
                            showAlert("Balance Low", "Balance is $0.00. Perform chore to get balance and request activities");
                        }
                        else if (getUserBalance(currentUsername)-price[activityList.indexOf(newValue)]<=0.00) {
                            insertMessage(currentUsername, "Not enough balance to get the activity requested");
                            showAlert("Not Enough Balance", "The activity requested is too pricey!");
                        }
                        else if (newValue != null) {
                            // Find the index of the selected activity
                            int num = activityList.indexOf(newValue);
                            // --- START OF MODIFICATION ---
                            if (num != -1 && num < price.length) { // Ensure the selected item exists in the list and index is within price array bounds
                                double val = price[num];
                                transactions.setVisible(true);
                                activityprice.setVisible(true);
                                activityprice.setText("-$" + String.format("%.2f", val)); // Display cost with sign
                                activity = "Selected Activity: " + newValue;
                                // Update user's balance for activity redemption
                                if (currentUsername != null) {
                                    updateUserBalance(currentUsername, -val, activity); // Subtract cost from balance
                                    // Insert the message into the database
                                    insertMessage(currentUsername, "Redeemed activity: " + newValue + " (-$" + String.format("%.2f", val) + ")");
                                }
                            } else {
                                System.err.println("Error: Selected activity '" + newValue + "' not found in activity list or price array mismatch. This should not happen if the list is populated correctly.");
                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Navigating to other page ling methods
    public void Activity() throws IOException {
        if (userName != null) {
            String un = userName.getText();
            app.setRoot("activitylist", un);
        }
    }

    public void notifications() throws IOException {
        if (userName != null) {
            String un = userName.getText();
            app.setRoot("notifications", un);
        }
    }

    public void Chores() throws IOException {
        if (userName != null) {
            String un = userName.getText();
            app.setRoot("choreslist", un);
        }
    }

    public void dashboard() throws IOException {
        if (userName != null) {
            String un = userName.getText();
            app.setRoot("primary", un);
        }
    }

    public void logout() throws IOException {
        app.setRoot("login");
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public boolean showInputDialog(String title, String headerText, String contentText, String defaultValue) throws IOException {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(headerText); // Can be null as in showAlert
        dialog.setContentText(contentText);

        // Show the dialog and wait for the user's response
        Optional<String> result = dialog.showAndWait();

        // Process the result
        if (result.isPresent() && dataValidation.validateLogin(currentUsername, result.get())) {
            return true; // Return the user's input
        } else {
            return false; // User cancelled the dialog
        }
    }
}