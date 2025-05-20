package com.example.smarthome3.controllers;

import com.example.smarthome3.Database.DatabaseConnector;
import com.example.smarthome3.Database.UserDAO;
import com.example.smarthome3.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private ChoiceBox<String> acc_selector;

    @FXML
    private TextField emailField;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private TextField usernameField;

    @FXML
    private Button SignInBtn1;

    private UserDAO userDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the database connection and DAO
        DatabaseConnector dbConnector = new DatabaseConnector();
        userDAO = new UserDAO(dbConnector);

        // Populate account type options
        acc_selector.setItems(FXCollections.observableArrayList(
                AccountType.HOMEOWNER.name(),
                AccountType.TECHNICIAN.name(),
                AccountType.SECURITYGUARD.name()
        ));
        acc_selector.setValue(AccountType.HOMEOWNER.name());

        // Sign-up button action
        signUpBtn.setOnAction(event -> onSignup());

        // Navigate to the login screen
        SignInBtn1.setOnAction(event -> openLoginDashboard());
    }

    private void onSignup() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText();
        String accountType = acc_selector.getValue();

        // Validation for empty fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill all fields.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        // Store plain password (no hashing)
        String plainPassword = password; // Use plain text password

        // Create user in the database
        boolean isCreated = userDAO.createUser(username, email, plainPassword, accountType);
        if (isCreated) {
            showAlert(Alert.AlertType.INFORMATION, "Sign-Up Successful", "You have successfully signed up!");
            openLoginDashboard();
        } else {
            showAlert(Alert.AlertType.ERROR, "Sign-Up Failed", "Could not create user. Please try again.");
        }
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Navigate to the login screen after successful sign up
    private void openLoginDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Login Dashboard");
            stage.show();
            ((Stage) SignInBtn1.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the login page.");
        }
    }
}
