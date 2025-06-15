package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField user;
    @FXML
    private PasswordField pw;

    private App app; // Reference to the App instance

    UserManager dataValidation = new UserManager("src/main/java/com/example/data.txt");

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    public void logins() throws IOException {
        System.out.println("LoginController.logins() called");

        String username = user.getText();
        String password = pw.getText();

        System.out.println("Username: " + username + ", Password: " + password);

        if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
             // Validate login
             if (dataValidation.validateLogin(username, password)) {
                System.out.println("Login successful.");
                System.out.println("Fields are valid. Switching to primary.");
                app.setRoot("primary", username);
            } else {
                showAlert("Error", "Invalid credentials.");
            }
        } else {
            System.out.println("Fields are invalid.");
            showAlert("Error", "Username and password are required.");
        }
    }
    /*@FXML
    private boolean isTextFieldFilled(TextField field) {
        return field != null && field.getText() != null && !field.getText().trim().isEmpty();
    }
    @FXML
    private boolean isPwFieldFilled(PasswordField field) {
        return field != null && field.getText() != null && !field.getText().trim().isEmpty();
    }*/
    @FXML
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void register() throws IOException {
        app.setRoot("register");
    }
    public void changePw() throws IOException {
        app.setRoot("passwordforgot");
    }
}