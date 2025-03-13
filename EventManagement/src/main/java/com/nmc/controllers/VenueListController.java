/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nmc.controllers;

/**
 *
 * @author FPTSHOP
 */
import com.nmc.pojo.Venue;
import com.nmc.services.VenueService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.cell.PropertyValueFactory;

public class VenueListController implements Initializable {

    @FXML
    private TableView<Venue> venueTable;
    @FXML
    private TableColumn<Venue, Integer> colId;
    @FXML
    private TableColumn<Venue, String> colName;
    @FXML
    private TableColumn<Venue, String> colLocation;
    @FXML
    private TableColumn<Venue, Integer> colCapacity;
    @FXML
    private TextField searchField;

    private final VenueService venueService = new VenueService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadVenues(null);

        // Tự động điều chỉnh kích thước cột
        venueTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colId.prefWidthProperty().bind(venueTable.widthProperty().multiply(0.1)); // 10%
        colName.prefWidthProperty().bind(venueTable.widthProperty().multiply(0.3)); // 30%
        colLocation.prefWidthProperty().bind(venueTable.widthProperty().multiply(0.4)); // 40%
        colCapacity.prefWidthProperty().bind(venueTable.widthProperty().multiply(0.2)); // 20%

        // Lắng nghe thay đổi trong ô tìm kiếm
        this.searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadVenues(newValue);
        });
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
    }

    private void loadVenues(String keyword) {
        try {
            List<Venue> venues;
            if (keyword == null || keyword.trim().isEmpty()) {
                venues = venueService.getAllVenues(); // Lấy tất cả địa điểm
            } else {
                venues = venueService.getVenuesByKeyword(keyword); // Tìm kiếm theo từ khóa
            }

            ObservableList<Venue> venueList = FXCollections.observableArrayList(venues);
            venueTable.setItems(venueList);
        } catch (SQLException ex) {
            Logger.getLogger(VenueListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nmc/fxml/home_content.fxml"));
            Parent homeContent = loader.load();

            // Lấy BorderPane từ Stage hiện tại
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            BorderPane rootPane = (BorderPane) stage.getScene().getRoot();

            // Cập nhật nội dung của <center> mà không thay đổi Scene
            rootPane.setCenter(homeContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddVenue(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nmc/fxml/venue_form.fxml"));
            Parent venueForm = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(venueForm));
            stage.setTitle("Thêm địa điểm");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @FXML
//    private void handleEditVenue(ActionEvent event) {
//        Venue selectedVenue = venueTable.getSelectionModel().getSelectedItem();
//        if (selectedVenue == null) {
//            showAlert("Vui lòng chọn một địa điểm để sửa.");
//            return;
//        }
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nmc/fxml/venue_form.fxml"));
//            Parent venueForm = loader.load();
//
//            VenueFormController controller = loader.getController();
//            controller.setVenue(selectedVenue); // Truyền dữ liệu cho form
//
//            Stage stage = new Stage();
//            stage.setScene(new Scene(venueForm));
//            stage.setTitle("Chỉnh sửa địa điểm");
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @FXML
    private void handleDeleteVenue(ActionEvent event) {
        Venue selectedVenue = venueTable.getSelectionModel().getSelectedItem();
        if (selectedVenue == null) {
            showAlert("Vui lòng chọn một địa điểm để xóa.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa địa điểm này?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    venueService.deleteVenue(selectedVenue.getId());
                } catch (SQLException ex) {
                    Logger.getLogger(VenueListController.class.getName()).log(Level.SEVERE, null, ex);
                }
                loadVenues(null); // Cập nhật lại danh sách
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

