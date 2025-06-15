package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    UserManager dataValidation = new UserManager("src/main/java/com/example/data.txt");

    @FXML
    private TextField newUsernameField;

    @FXML
    private PasswordField newPasswordField;

    private App app; // Reference to the main App instance

    // This method is called by App.java to inject the App instance
    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    private void handleRegisterButton() throws IOException {
        String username = newUsernameField.getText();
        String password = newPasswordField.getText();

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            showAlert("Registration Error", "Username and password cannot be empty.");
        } else {
            // In a real application, you would save this username/password to a database
            // For now, we'll just print them and show a success message.
            if (dataValidation.addUser(username, password)) {
                showAlert("Sucessful", "User added successfully.");
                showAlert("Registration Success", "Account created for " + username + "!");
                if (app != null) {
                    app.setRoot("login"); // Go back to the login page
                }
            } else {
                showAlert("Registration Error", "Username already exists.");
            }
        }
    }

    @FXML
    private void backToLogin() throws IOException {
        //if (app != null) {
           app.setRoot("login"); // Navigate back to the login page
        //}
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}