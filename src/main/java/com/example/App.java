package com.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Connection; // Import Connection
import java.sql.DriverManager; // Import DriverManager
import java.sql.SQLException; // Import SQLException
import java.sql.Statement; // Import Statement

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    private static App instance;
    private Connection connection; // Declare the Connection object
    private static final String DB_URL = "jdbc:sqlite:app_data.db"; // Database URL

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        primaryStage = stage;

        // Establish the database connection when the app starts
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connection established by App.java.");
            // Initialize database schema (tables) here or in the first controller
            initializeDatabase(connection);
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database on startup: " + e.getMessage());
            // Handle this error appropriately, perhaps show an alert and exit
            showAlert("Database Error", "Failed to connect to the database. Please ensure it's accessible.");
            System.exit(1); // Exit if database connection fails
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root;
        try {
            root = loader.load();
            LoginController controller = loader.getController();
            if (controller == null) {
                throw new IOException("LoginController is null after loading FXML");
            }
            controller.setApp(this);

            scene = new Scene(root, 640, 480);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading login.fxml: " + e.getMessage());
            showAlert("Error", "Failed to load the application. Please try again.");
            System.exit(1);
        }
    }

    // Method to initialize the database schema (create tables)
    // This is a good place to ensure tables exist once at app startup
    private void initializeDatabase(Connection conn) {
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                                   + "username TEXT PRIMARY KEY,"
                                   + "balance REAL DEFAULT 0.00"
                                   + ");";
        String createMessagesTableSQL = "CREATE TABLE IF NOT EXISTS messages ("
                                      + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                      + "username TEXT NOT NULL,"
                                      + "message TEXT NOT NULL,"
                                      + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,"
                                      + "FOREIGN KEY (username) REFERENCES users(username)"
                                      + ");";
        try (Statement stmt = conn.createStatement()) { // Use Statement for simple execute
            stmt.execute(createUsersTableSQL);
            stmt.execute(createMessagesTableSQL);
            System.out.println("Users and Messages tables checked/created by App.java.");
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
            showAlert("Database Initialization Error", "Failed to initialize database tables.");
        }
    }

    // Overloaded setRoot method to accept a username
    public void setRoot(String fxml, String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent newRoot = loader.load();

        Object controller = loader.getController();
        if (controller instanceof LoginController) {
            ((LoginController) controller).setApp(App.instance);
        } else if (controller instanceof RegisterController) {
            ((RegisterController) controller).setApp(App.instance);
        } else if (controller instanceof PrimaryController) {
            PrimaryController primaryController = (PrimaryController) controller;
            primaryController.setApp(App.instance);
            primaryController.setConnection(connection); // Pass the connection FIRST
            primaryController.setUsername(username);    // Then set the username
        }else if (controller instanceof ChoresList) {
            ((ChoresList) controller).setApp(App.instance);
            ((ChoresList) controller).setUsername(username);
        }else if (controller instanceof ActivityList) {
            ((ActivityList) controller).setApp(App.instance);
            ((ActivityList) controller).setUsername(username);
        }else if (controller instanceof ForgotPassword) {
            ((ForgotPassword) controller).setApp(App.instance);
        }else if (controller instanceof Notifications) { // Handle Notifications controller
            Notifications notificationsController = (Notifications) controller;
            notificationsController.setApp(App.instance);
            notificationsController.setConnection(connection); // Pass the connection FIRST
            notificationsController.setUsername(username);    // Then set the username
        }

        if (scene != null) {
            scene.setRoot(newRoot);
            if (primaryStage != null) {
                 primaryStage.setTitle(fxml.substring(0, 1).toUpperCase() + fxml.substring(1));
            }
        } else {
            System.err.println("Scene is null when trying to set new root. This indicates a problem in flow.");
            if (primaryStage != null) {
                scene = new Scene(newRoot, 640, 480);
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                System.err.println("Primary stage is null. Cannot display new scene.");
            }
        }
    }

    // Original setRoot method (if you still need it for FXMLs without username)
    public void setRoot(String fxml) throws IOException {
        setRoot(fxml, null); // Call the overloaded method with a null username
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() {
        // Close the database connection when the application exits
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed by App.java.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}