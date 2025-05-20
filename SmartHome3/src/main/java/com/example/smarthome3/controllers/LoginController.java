package com.example.smarthome3.controllers;

import com.example.smarthome3.Models.Model;
import com.example.smarthome3.Views.AccountType;
import com.example.smarthome3.Database.DatabaseConnector;
import com.example.smarthome3.Database.UserDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private ChoiceBox<AccountType> acc_selector;

    @FXML
    private TextField user_address_lbl;  // This will now represent the username field

    @FXML
    private Button login_btn;
    @FXML
    private Button Signin_btn;

    @FXML
    private TextField password_fld;

    private UserDAO userDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        userDAO = new UserDAO(dbConnector);

        // Populate account types
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.HOMEOWNER, AccountType.TECHNICIAN, AccountType.SECURITYGUARD));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable ->
                Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue())
        );

        // Set the login button action
        login_btn.setOnAction(event -> onLogin());
        Signin_btn.setOnAction(event -> openSignUpWindow());
    }

    private void onLogin() {
        String username = user_address_lbl.getText().trim();  // Use username now
        String password = password_fld.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Login Error", "Username or password field is empty.");
            return;
        }

        try {
            // Fetch user by username (instead of email)
            ResultSet result = userDAO.getUserByUsername(username);  // Updated method to get user by username
            if (result != null && result.next()) {
                String storedPassword = result.getString("password");
                String role = result.getString("accountType");

                // Directly compare the plain text password (not recommended in production)
                if (storedPassword.equals(password)) {
                    AccountType userRole = AccountType.valueOf(role.toUpperCase());

                    // Store login info in model
                    Model.getInstance().setUsername(username);  // Store username, not email
                    Model.getInstance().setUserRole(userRole);

                    System.out.println("✅ Login successful as " + role);
                    openDashboard(userRole);
                } else {
                    showAlert(AlertType.ERROR, "Login Error", "Incorrect password.");
                }
            } else {
                showAlert(AlertType.ERROR, "Login Error", "User not found.");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error occurred while accessing the database.");
            e.printStackTrace();
        }
    }

    private void openDashboard(AccountType accountType) {
        Stage stage = (Stage) login_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);

        // Open the corresponding dashboard window based on account type
        switch (accountType) {
            case HOMEOWNER:
                Model.getInstance().getViewFactory().showHomeownerWindow();
                break;
            case TECHNICIAN:
                Model.getInstance().getViewFactory().showTechnicianWindow();
                break;
            case SECURITYGUARD:
                Model.getInstance().getViewFactory().showSecurityGuardWindow();
                break;
            default:
                System.out.println("❌ Unknown account type.");
                showAlert(AlertType.ERROR, "Unknown Account", "Unknown account type. Please contact support.");
        }
    }
    private void openSignUpWindow() {
        Stage stage = (Stage) Signin_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showSignUpWindow();  // You must implement this in ViewFactory
    }

    // Utility method to show alert dialogs
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
