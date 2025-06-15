package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class ForgotPassword {

    @FXML
    private TextField user;
    @FXML
    private ToggleButton activation;
    @FXML
    private ToggleButton Enter;
    @FXML
    private TextField Pw;
    @FXML
    private PasswordField Pwagain;
    @FXML
    private Label Pwlabel;
    @FXML
    private Label Pwagainlabel;

    private App app; // Reference to the App instance

    UserManager dataValidation = new UserManager("src/main/java/com/example/data.txt");

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    public void forgotPassword() throws IOException {
        System.out.println("PasswordForgot.forgotPassword() called");

        String username = user.getText();

        System.out.println("Username: " + username);

        if (username != null && !username.trim().isEmpty()) {
             // Validate login
             if (dataValidation.checkUser(username)) {
                showAlert("Successful", "Username Matched");
                activation.setVisible(true);
                Enter.setVisible(false);
                Pw.setVisible(true);
                Pwagain.setVisible(true);
                Pwlabel.setVisible(true);
                Pwagainlabel.setVisible(true);
            }else {
                showAlert("Error", "Invalid username.");
            }
        } else {
            System.out.println("Username invalid.");
            showAlert("Error", "Username is required.");
        }
    }
    @FXML
    public void newpw() throws IOException{
        String username = user.getText();
        if (Pw.getText().equals(Pwagain.getText())){
            dataValidation.changePass(username, Pw.getText());
            showAlert("Success", "Password changed for "+username+"!");
            app.setRoot("login");
        }else{
            showAlert("Error", "Passwords don't match!");
        }
    }

    @FXML
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void backToLogin() throws IOException {
        //if (app != null) {
           app.setRoot("login"); // Navigate back to the login page
        //}
    }
}