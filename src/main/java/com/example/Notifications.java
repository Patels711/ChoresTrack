package com.example;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Connection; // Import Connection
import java.sql.PreparedStatement; // Import PreparedStatement
import java.sql.ResultSet; // Import ResultSet
import java.sql.SQLException; // Import SQLException

public class Notifications {

    @FXML
    private Text userName;
    @FXML
    private Text selection;

    @FXML
    private TableView<MessageItem> messageTable; // Changed to messageTable and MessageItem

    @FXML
    private TableColumn<MessageItem, String> messageColumn; // Changed to messageColumn

    @FXML
    private TableColumn<MessageItem, String> timestampColumn; // Changed to timestampColumn

    private App app;
    private String currentUsername; // To store the logged-in username
    private Connection connection; // To hold the passed database connection

    public void setApp(App app) {
        this.app = app;
    }

    // New method to set the database connection
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setUsername(String username) {
        this.currentUsername = username;
        if (userName != null) {
            userName.setText(username);
        }
        // Fetch and display messages when username is set
        loadMessages();
    }

    @FXML
    public void initialize() {
        // Ensure columns and table are not null before proceeding
        if (messageColumn != null && timestampColumn != null && messageTable != null) {
            // Set up cell value factories for the columns
            // "message" corresponds to the getMessage() method in MessageItem
            messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
            // "timestamp" corresponds to the getTimestamp() method in MessageItem
            timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
            // Initial load of messages will happen when setUsername is called
            // No need to populate with static data anymore.
        }
        // Add listener for row clicks
        messageTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selection.setText( newSelection.getMessage() + " " + newSelection.getTimestamp());
                selection.setVisible(true);
            }
        });
    }

    // Method to load messages from the database for the current user
    private void loadMessages() {
        ObservableList<MessageItem> messages = FXCollections.observableArrayList();
        String sql = "SELECT message, timestamp FROM messages WHERE username = ? ORDER BY timestamp DESC";

        if (this.connection == null) {
            System.err.println("Error: Database connection is null in loadMessages.");
            return;
        }
        try {
            if (this.connection.isClosed()) {
                System.err.println("Error: Database connection is closed in loadMessages.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status in loadMessages: " + e.getMessage());
            return;
        }

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, currentUsername);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                messages.add(new MessageItem(rs.getString("message"), rs.getString("timestamp")));
            }
            messageTable.setItems(messages);

        } catch (SQLException e) {
            System.err.println("Error fetching messages for user " + currentUsername + ": " + e.getMessage());
        }
    }

    // --- Navigation Methods ---
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
    // Nested static class to represent table rows for messages
    // Renamed from RewardItem to MessageItem
    public static class MessageItem {
        private final String message;
        private final String timestamp;

        /**
         * Constructor for MessageItem.
         * @param message The message text.
         * @param timestamp The timestamp of the message.
         */
        public MessageItem(String message, String timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }

        /**
         * Getter for the message text.
         * @return The message text.
         */
        public String getMessage() {
            return message;
        }

        /**
         * Getter for the timestamp.
         * @return The timestamp.
         */
        public String getTimestamp() {
            return timestamp;
        }
    }
}