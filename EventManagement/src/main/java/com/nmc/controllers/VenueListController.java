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
import javafx.stage.Modality;

public class VenueListController implements Initializable {

    @FXML
    private TableView<Venue> venueTable;
    private static ObservableList<Venue> venueList = FXCollections.observableArrayList();

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
    @FXML
    private Button btnEdit, btnDelete;

    private final VenueService venueService = new VenueService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEdit.setOnAction(event -> handleEditVenue());
        btnDelete.setOnAction(event -> handleDeleteVenue());
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

        btnEdit.setDisable(true); // Vô hiệu hóa nút "Sửa" ban đầu
        btnDelete.setDisable(true); // Vô hiệu hóa nút "Xóa" ban đầu

        // Khi chọn hàng, bật nút "Sửa"
        venueTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnEdit.setDisable(newSelection == null);
            btnDelete.setDisable(newSelection == null);
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

            venueList.setAll(venues);
            venueTable.setItems(venueList);
        } catch (SQLException ex) {
            Logger.getLogger(VenueListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleAddVenue(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nmc/fxml/venue_form.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Thêm địa điểm");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteVenue() {
        Venue selectedVenue = venueTable.getSelectionModel().getSelectedItem();
        if (selectedVenue != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn xóa địa điểm này không?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    if (venueService.deleteVenue(selectedVenue.getId())) {
                        showAlert(Alert.AlertType.INFORMATION, "Xóa thành công!", "Địa điểm đã bị xóa.");
                        loadVenues(null); // Cập nhật lại danh sách trên giao diện
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Lỗi!", "Không thể xóa địa điểm.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi!", "Lỗi khi xóa địa điểm: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleEditVenue() {
        Venue selectedVenue = venueTable.getSelectionModel().getSelectedItem();
        if (selectedVenue != null) {
            openVenueDetailForm(selectedVenue);
        }
    }

    private void openVenueDetailForm(Venue venue) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nmc/fxml/venue_detail.fxml"));
            Parent root = loader.load();

            // Lấy controller của VenueDetail
            VenueDetailController controller = loader.getController();
            controller.setVenue(venue); // Truyền dữ liệu vào form

            Stage stage = new Stage();
            stage.setTitle("Chỉnh sửa địa điểm");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void loadVenuesFromDB() {
        try {
            VenueService venueService = new VenueService();
            List<Venue> venues = venueService.getAllVenues(); // Hàm lấy dữ liệu từ database
            venueList.setAll(venues); // Cập nhật lại danh sách
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
