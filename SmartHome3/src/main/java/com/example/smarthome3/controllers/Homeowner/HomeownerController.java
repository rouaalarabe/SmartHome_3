package com.example.smarthome3.controllers.Homeowner;
import com.example.smarthome3.Database.DatabaseConnector;
import com.example.smarthome3.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeownerController implements Initializable {

    @FXML private BorderPane homeowner_parent;

    @FXML private LineChart<String, Number> humidityChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    @FXML private LineChart<String, Number> temperatureChart;
    @FXML private CategoryAxis yAxis1;
    @FXML private NumberAxis xAxis1;

    @FXML private LineChart<String, Number> lightChart;
    @FXML private CategoryAxis lightXAxis;  // Time axis
    @FXML private NumberAxis lightYAxis;    // Lux values

    @FXML public ListView<String> humidity_listview;
    @FXML public ListView<String> temperature_listview;
    @FXML public ListView<String> light_listview;

    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initializing HomeownerController...");

        connection = DatabaseConnector.getConnection();

        // Humidity Chart
        if (xAxis != null && yAxis != null && humidityChart != null) {
            xAxis.setLabel("Time (Days)");
            yAxis.setLabel("Humidity (%)");
            yAxis.setLowerBound(0);
            yAxis.setUpperBound(100);
            loadHumidityData();
        }

        // Temperature Chart
        if (xAxis1 != null && yAxis1 != null && temperatureChart != null) {
            xAxis1.setLabel("Time (Days)");
            yAxis1.setLabel("Temperature (°C)");
            yAxis1.setAutoRanging(true);
            loadTemperatureData();
        }

        // Light Chart
        if (lightXAxis != null && lightYAxis != null && lightChart != null) {
            lightXAxis.setLabel("Time (Hours)");
            lightYAxis.setLabel("Light Lux");
            lightYAxis.setAutoRanging(true);
            loadLightData();
        }

        // Menu Switching
        Model.getInstance().getViewFactory().getHomeownerSelectedMenuItem().addListener((obs, oldVal, newVal) -> {
            Node view = switch (newVal) {
                case "Humidity" -> Model.getInstance().getViewFactory().getHumidityView();
                case "Temperature" -> Model.getInstance().getViewFactory().getTemperatureView();
                case "Motion" -> Model.getInstance().getViewFactory().getMotionView();
                case "Light" -> Model.getInstance().getViewFactory().getLightView();
                default -> Model.getInstance().getViewFactory().getDashboardView();
            };

            if (homeowner_parent != null && view != null) {
                homeowner_parent.setCenter(view);
            }
        });
    }

    private void loadTemperatureData() {
        if (connection == null) return;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Temperature");

        String query = "SELECT recorded_at, temperature FROM sensor_data WHERE temperature IS NOT NULL ORDER BY recorded_at";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (temperature_listview != null) temperature_listview.getItems().clear();

            while (rs.next()) {
                String timestamp = rs.getString("recorded_at");
                double temp = rs.getDouble("temperature");
                series.getData().add(new XYChart.Data<>(timestamp, temp));

                if (temperature_listview != null)
                    temperature_listview.getItems().add(timestamp + ": " + String.format("%.2f", temp) + "°C");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        temperatureChart.getData().clear();
        temperatureChart.getData().add(series);
        temperatureChart.layout();
    }

    private void loadHumidityData() {
        if (connection == null) return;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Humidity");

        String query = "SELECT recorded_at, humidity FROM sensor_data WHERE humidity IS NOT NULL ORDER BY recorded_at";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (humidity_listview != null) humidity_listview.getItems().clear();

            while (rs.next()) {
                String timestamp = rs.getString("recorded_at");
                double humidity = rs.getDouble("humidity");
                series.getData().add(new XYChart.Data<>(timestamp, humidity));

                if (humidity_listview != null)
                    humidity_listview.getItems().add(timestamp + ": " + String.format("%.2f", humidity) + "%");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        humidityChart.getData().clear();
        humidityChart.getData().add(series);
        humidityChart.layout();
    }

    private void loadLightData() {
        if (connection == null) return;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Light_Lux");

        String query = "SELECT recorded_at, light_lux FROM sensor_data WHERE light_lux IS NOT NULL ORDER BY recorded_at";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (light_listview != null) light_listview.getItems().clear();

            while (rs.next()) {
                String timestamp = rs.getString("recorded_at");
                double light = rs.getDouble("light_lux");
                series.getData().add(new XYChart.Data<>(timestamp, light));

                if (light_listview != null)
                    light_listview.getItems().add(timestamp + ": " + String.format("%.2f", light) + " lx");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        lightChart.getData().clear();
        lightChart.getData().add(series);
        lightChart.layout();
    }
}
