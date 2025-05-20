package com.example.smarthome3.Views;


import com.example.smarthome3.controllers.Homeowner.HomeownerController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewFactory {
    private AccountType LoginAccountType;
    // Homeowner Views
    private final StringProperty HomeownerSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane humidityView;
    private AnchorPane temperatureView;
    private AnchorPane motionView;
    private AnchorPane lightView;
    // Technician Views


    public ViewFactory() {
        this.LoginAccountType = AccountType.HOMEOWNER;
        this.HomeownerSelectedMenuItem = new SimpleStringProperty();
    }

    public AccountType getLoginAccountType() {
        return LoginAccountType;
    }

    public void setLoginAccountType(AccountType LoginAccountType) {
        this.LoginAccountType = LoginAccountType;
    }

    public StringProperty getHomeownerSelectedMenuItem() {
        return HomeownerSelectedMenuItem;
    }

    public AnchorPane getDashboardView() {
        if (dashboardView == null) {
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Homeowner/Dashboard.fxml")).load();
            } catch (Exception e) {
                System.out.println("Error loading Dashboard.fxml: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return dashboardView;
    }


    public AnchorPane getHumidityView() {
        if (humidityView == null) {
            try {
                humidityView = new FXMLLoader(getClass().getResource("/Fxml/Homeowner/Humidity.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return humidityView;
    }

    public AnchorPane getTemperatureView() {
        if (temperatureView == null) {
            try {
                System.out.println("Loading Temperature view...");
                temperatureView = new FXMLLoader(getClass().getResource("/Fxml/Homeowner/Temperature.fxml")).load();
                System.out.println("✅ Temperature view loaded successfully.");
            } catch (Exception e) {
                System.out.println("❌ Error loading Temperature view: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return temperatureView;
    }

    public AnchorPane getMotionView() {
        if (motionView == null) {
            try {
                System.out.println("Loading Motion view...");
                motionView = new FXMLLoader(getClass().getResource("/Fxml/Homeowner/Motion.fxml")).load();
                System.out.println("✅ Motion view loaded successfully.");
            } catch (Exception e) {
                System.out.println("❌ Error loading Motion view: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return motionView;
    }

    public AnchorPane getLightView() {
        if (lightView == null) {
            try {
                System.out.println("Loading Light view...");
                lightView = new FXMLLoader(getClass().getResource("/Fxml/Homeowner/Light.fxml")).load();
                System.out.println("✅ Light view loaded successfully.");
            } catch (Exception e) {
                System.out.println("❌ Error loading Light view: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return lightView;
    }
    //


    public void showHomeownerWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Homeowner/Homeowner.fxml"));
            HomeownerController homeownerController = new HomeownerController();
            loader.setController(homeownerController);
            BorderPane root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Smart Home - Homeowner");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTechnicianWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Technician/Technician.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Smart Home - Technician");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSecurityGuardWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/SecurityGuard/SecurityGuard.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Smart Home - Security Guard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading Security Guard Dashboard");
        }
    }

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Smart Home - Login");
        stage.show();
    }

    public void showSignUpWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Signup.fxml")); // or Signup.fxml if that's your real file
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Smart Home - Sign In");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading Sign In window");
        }
    }

    public void closeStage(Stage stage) {

        stage.close();


    }

}