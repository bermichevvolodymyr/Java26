package com.example;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class AppController {
    @FXML private TextField tfId, tfName, tfLatitude, tfLongitude, tfRegion, tfImage;
    @FXML private TableView<Site> tvSites;
    @FXML private TableColumn<Site, Integer> colId;
    @FXML private TableColumn<Site, String> colName;
    @FXML private TableColumn<Site, Double> colLatitude;
    @FXML private TableColumn<Site, Double> colLongitude;
    @FXML private TableColumn<Site, String> colRegion;
    @FXML private TableColumn<Site, String> colImage;

    private ObservableList<Site> siteList = FXCollections.observableArrayList();
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db";
    private static final String USER = "root";
    private static final String PASS = "Admin_Pass123!";

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        colName.setCellValueFactory(c -> c.getValue().nameProperty());
        colLatitude.setCellValueFactory(c -> c.getValue().latitudeProperty().asObject());
        colLongitude.setCellValueFactory(c -> c.getValue().longitudeProperty().asObject());
        colRegion.setCellValueFactory(c -> c.getValue().regionProperty());
        colImage.setCellValueFactory(c -> c.getValue().imageProperty());
        showSites();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void showSites() {
        siteList.clear();
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM sites")) {
            while (rs.next()) {
                siteList.add(new Site(rs.getInt("id"), rs.getString("name"),
                    rs.getDouble("latitude"), rs.getDouble("longitude"),
                    rs.getString("region"), rs.getString("image")));
            }
            tvSites.setItems(siteList);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void insertRecord() {
        try (Connection conn = getConnection();
             PreparedStatement p = conn.prepareStatement(
                "INSERT INTO sites (name, latitude, longitude, region, image) VALUES (?, ?, ?, ?, ?)")) {
            p.setString(1, tfName.getText());
            p.setDouble(2, Double.parseDouble(tfLatitude.getText()));
            p.setDouble(3, Double.parseDouble(tfLongitude.getText()));
            p.setString(4, tfRegion.getText());
            p.setString(5, tfImage.getText());
            p.executeUpdate(); showSites(); clearFields();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void updateRecord() {
        try (Connection conn = getConnection();
             PreparedStatement p = conn.prepareStatement(
                "UPDATE sites SET name=?, latitude=?, longitude=?, region=?, image=? WHERE id=?")) {
            p.setString(1, tfName.getText());
            p.setDouble(2, Double.parseDouble(tfLatitude.getText()));
            p.setDouble(3, Double.parseDouble(tfLongitude.getText()));
            p.setString(4, tfRegion.getText());
            p.setString(5, tfImage.getText());
            p.setInt(6, Integer.parseInt(tfId.getText()));
            p.executeUpdate(); showSites();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void deleteRecord() {
        try (Connection conn = getConnection();
             PreparedStatement p = conn.prepareStatement("DELETE FROM sites WHERE id = ?")) {
            p.setInt(1, Integer.parseInt(tfId.getText()));
            p.executeUpdate(); showSites(); clearFields();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void clearFields() {
        tfId.clear(); tfName.clear(); tfLatitude.clear();
        tfLongitude.clear(); tfRegion.clear(); tfImage.clear();
    }
}