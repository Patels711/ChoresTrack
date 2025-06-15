package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserManager {
    private final String filePath;

    public UserManager(String filePath) {
        this.filePath = filePath;
    }

    // Add a new user
    public boolean addUser(String username, String password) throws IOException {
        if (usernameExists(username)) {
            return false;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(username + ", " + password);
            writer.newLine();
        }

        return true;
    }

    // Check if username exists
    public boolean usernameExists(String username) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        }

        return false;
    }
    public boolean validateLogin(String username, String password) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        }

        return false;
    }
    public boolean checkUser(String username) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2 && parts[0].equals(username)) {
                    return true;
                }
            }
        }

        return false;
    }
    /*// In UserManager.java*/
    public boolean changePass(String username, String newPassword) throws IOException {
        File inputFile = new File(filePath);
        // tempFile is a temporary file used only during the update process.
        // It gets renamed to inputFile, effectively replacing the original.
        File tempFile = new File("src/main/java/com/example/temp_data.txt");

        boolean passwordChanged = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2 && parts[0].equals(username)) {
                    writer.write(username + ", " + newPassword); // Write the updated line
                    writer.newLine();
                    passwordChanged = true;
                } else {
                    writer.write(line); // Write original line
                    writer.newLine();
                }
            }
        }

        // Replace the original file with the temporary file if changes were made
        if (passwordChanged) {
            if (!inputFile.delete()) {
                System.err.println("Could not delete original file: " + inputFile.getAbsolutePath());
                return false;
            }
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Could not rename temporary file to original: " + tempFile.getAbsolutePath());
                return false;
            }
        }
        return passwordChanged;
    }
}

