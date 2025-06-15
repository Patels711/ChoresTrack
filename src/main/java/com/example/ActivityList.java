package com.example;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class ActivityList {
    @FXML
    private Text userName;
    @FXML
    private Text selection;

    @FXML
    private TableView<RewardItem> rewardTable;

    @FXML
    private TableColumn<RewardItem, String> rewardColumn;

    // Removed pointsColumn
    // @FXML
    // private TableColumn<RewardItem, String> pointsColumn; // THIS LINE IS REMOVED

    @FXML
    private TableColumn<RewardItem, String> valueColumn;

    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    public void initialize() {
        // Adjusted the null check to exclude pointsColumn
        if (rewardColumn != null && valueColumn != null && rewardTable != null) {
            rewardColumn.setCellValueFactory(new PropertyValueFactory<>("reward"));
            // Removed the line for pointsColumn:
            // pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
            valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

            ObservableList<RewardItem> rewards = FXCollections.observableArrayList(
                // Updated RewardItem instantiations with only Reward and Value
                new RewardItem("15 minutes of screen time", "$0.50"),
                new RewardItem("30 minutes of screen time", "$1"),
                new RewardItem("Choose the family movie or show", "$1.50"),
                new RewardItem("Extra 30 minutes before bedtime", "$1.50"),
                new RewardItem("Pick what’s for dinner", "$2"),
                new RewardItem("Small treat or snack (candy, chips)", "$1"),
                new RewardItem("Ice cream outing", "$3"),
                new RewardItem("Baking with a parent", "$2"),
                new RewardItem("$5 gift card", "$5"),
                new RewardItem("$10 gift card", "$10"),
                new RewardItem("Download a new app or game", "$6"),
                new RewardItem("Trip to the park, pool, or fun center", "$5"),
                new RewardItem("Invite a friend over", "$2.50"),
                new RewardItem("Go to a movie theater", "$6"),
                new RewardItem("One “no chore” pass", "$4"),
                new RewardItem("Choose music on car rides for the day", "$1"),
                new RewardItem("Craft/art session with supplies", "$2.50"),
                new RewardItem("30 minutes of LEGO/building time", "$1"),
                new RewardItem("Hobby/project time (drawing, music, etc.)", "$2"),
                new RewardItem("1-hour gaming session", "$2")
            );

            // Set the items to the rewardTable
            rewardTable.setItems(rewards);

        // Add listener for row clicks
        rewardTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selection.setText("Selected Activity: " + newSelection.getReward() + ", Value: " + newSelection.getValue());
                selection.setVisible(true);
            }
        });
    }
    }

    // Navigation methods
    public void setUsername(String username) {
        if (userName != null) {
            userName.setText(username);
        }
    }
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

    // Nested static class to represent table rows
    public static class RewardItem {
        private final String reward;
        // Removed points field
        // private final String points; // THIS LINE IS REMOVED
        private final String value;

        // Updated constructor to only take reward and value
        public RewardItem(String reward, String value) {
            this.reward = reward;
            this.value = value;
        }

        public String getReward() {
            return reward;
        }

        // Removed getPoints() method
        // public String getPoints() { // THIS METHOD IS REMOVED
        //     return points;
        // }

        public String getValue() {
            return value;
        }
    }
}