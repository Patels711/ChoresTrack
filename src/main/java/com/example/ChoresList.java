package com.example;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class ChoresList {

    @FXML
    private Text userName;
    @FXML
    private Text selection;

    @FXML
    private TableView<RewardItem> rewardTable;

    @FXML
    private TableColumn<RewardItem, String> rewardColumn;

    @FXML
    private TableColumn<RewardItem, String> valueColumn;

    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    public void setUsername(String username) {
        if (userName != null) {
            userName.setText(username);
        }
    }

    @FXML
    public void initialize() {
        // Ensure rewardColumn, valueColumn, and rewardTable are not null before proceeding
        if (rewardColumn != null && valueColumn != null && rewardTable != null) {
            // Set up cell value factories for the columns
            // "reward" corresponds to the getReward() method in RewardItem
            rewardColumn.setCellValueFactory(new PropertyValueFactory<>("reward"));
            // "value" corresponds to the getValue() method in RewardItem
            valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

            // Create an ObservableList to hold the reward items
            ObservableList<RewardItem> rewards = FXCollections.observableArrayList(
                // New RewardItem instantiations based on the provided Chore and Value table
                new RewardItem("Make your bed (daily)", "$0.25"),
                new RewardItem("Clean up your room", "$1.00"),
                new RewardItem("Do the dishes/load dishwasher", "$1.00"),
                new RewardItem("Unload dishwasher", "$0.75"),
                new RewardItem("Take out the trash", "$0.50"),
                new RewardItem("Vacuum one room", "$0.75"),
                new RewardItem("Vacuum the whole house", "$2.00"),
                new RewardItem("Sweep and mop floors", "$2.00"),
                new RewardItem("Clean the bathroom", "$3.00"),
                new RewardItem("Fold laundry", "$1.00"),
                new RewardItem("Do a full load of laundry (wash + dry + fold)", "$2.50"),
                new RewardItem("Water indoor plants", "$0.50"),
                new RewardItem("Feed pets", "$0.50"),
                new RewardItem("Walk the dog (15–30 mins)", "$1.50"),
                new RewardItem("Yard work (pull weeds, rake, etc.)", "$3.00"),
                new RewardItem("Mow the lawn", "$5.00"),
                new RewardItem("Wash the car", "$4.00"),
                new RewardItem("Clean windows/mirrors", "$1.50"),
                new RewardItem("Organize a closet or drawer", "$2.00"),
                new RewardItem("Help cook dinner", "$2.00"),
                new RewardItem("Babysit younger sibling (1 hr)", "$5.00"),
                new RewardItem("Clean garage or basement (supervised)", "$4.00")
            );

            // Set the items to the rewardTable
            rewardTable.setItems(rewards);

        // Add listener for row clicks
        rewardTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selection.setText("Selected Chore: " + newSelection.getReward() + ", Value: " + newSelection.getValue());
                selection.setVisible(true);
            }
        });
    }
    }

    // Navigation methods
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

    // Nested static class to represent table rows for rewards/chores
    public static class RewardItem {
        private final String reward; // Represents the chore/activity name
        private final String value;  // Represents the monetary value

        /**
         * Constructor for RewardItem.
         * @param reward The name of the reward or chore.
         * @param value The monetary value associated with the reward or chore.
         */
        public RewardItem(String reward, String value) {
            this.reward = reward;
            this.value = value;
        }

        /**
         * Getter for the reward/chore name.
         * @return The reward/chore name.
         */
        public String getReward() {
            return reward;
        }

        /**
         * Getter for the monetary value.
         * @return The monetary value.
         */
        public String getValue() {
            return value;
        }
    }
}