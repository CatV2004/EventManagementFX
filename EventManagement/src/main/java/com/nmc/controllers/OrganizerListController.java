/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nmc.controllers;

/**
 *
 * @author FPTSHOP
 */
import com.nmc.pojo.Organizer;
import com.nmc.pojo.Venue;
import com.nmc.services.OrganizerService;
import com.nmc.services.VenueService;
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

public class OrganizerListController implements Initializable {

    @FXML
    private TableView<Organizer> organizerTable;
    private static ObservableList<Organizer> organizerList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Organizer, Integer> colId;
    @FXML
    private TableColumn<Organizer, String> colName;
    @FXML
    private TableColumn<Organizer, String> colContactPerson;
    @FXML
    private TableColumn<Organizer, String> colEmail;
    @FXML
    private TableColumn<Organizer, String> colPhone;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnEdit, btnDelete;

    private final OrganizerService organizerService = new OrganizerService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEdit.setOnAction(event -> handleEditOrganizer());
        btnDelete.setOnAction(event -> handleDeleteOrganizer());
        setupTableColumns();
        loadOrganizers(null);

        organizerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colId.prefWidthProperty().bind(organizerTable.widthProperty().multiply(0.1));
        colName.prefWidthProperty().bind(organizerTable.widthProperty().multiply(0.3));
        colContactPerson.prefWidthProperty().bind(organizerTable.widthProperty().multiply(0.2));
        colEmail.prefWidthProperty().bind(organizerTable.widthProperty().multiply(0.2));
        colPhone.prefWidthProperty().bind(organizerTable.widthProperty().multiply(0.2));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadOrganizers(newValue);
        });
        
        btnEdit.setDisable(true); // Vô hiệu hóa nút "Sửa" ban đầu
        btnDelete.setDisable(true); // Vô hiệu hóa nút "Xóa" ban đầu

        // Khi chọn hàng, bật nút "Sửa"
        organizerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnEdit.setDisable(newSelection == null);
            btnDelete.setDisable(newSelection == null);
        });
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContactPerson.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    private void loadOrganizers(String keyword) {
        try {
            List<Organizer> organizers;
            if (keyword == null || keyword.trim().isEmpty()) {
                organizers = organizerService.getAllOrganizers();
            } else {
                organizers = organizerService.getOrganizersByKeyword(keyword);
            }

            organizerList.setAll(organizers);
            organizerTable.setItems(organizerList);
        } catch (SQLException ex) {
            Logger.getLogger(VenueListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleAddOrganizer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nmc/fxml/organizer_form.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Thêm khách hàng");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteOrganizer() {
        Organizer selectedOrganizer = organizerTable.getSelectionModel().getSelectedItem();
        if (selectedOrganizer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn xóa khách hàng này không?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    if (organizerService.deleteOrganizer(selectedOrganizer.getId())) {
                        showAlert(Alert.AlertType.INFORMATION, "Xóa thành công!", "khách hàng đã bị xóa.");
                        loadOrganizers(null);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Lỗi!", "Không thể xóa khách hàng.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi!", "Lỗi khi xóa khách hàng: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleEditOrganizer() {
        Organizer selectedOrganizer = organizerTable.getSelectionModel().getSelectedItem();
        if (selectedOrganizer != null) {
            openOrganizerDetailForm(selectedOrganizer);
        }
    }

    private void openOrganizerDetailForm(Organizer organizer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nmc/fxml/organizer_detail.fxml"));
            Parent root = loader.load();

            // Lấy controller của OrganizerDetail
            OrganizerDetailController controller = loader.getController();
            controller.setOrganizer(organizer);

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

    public static void loadOrganizersFromDB() {
        try {
            OrganizerService organizerService = new OrganizerService();
            List<Organizer> organizers = organizerService.getAllOrganizers();
            organizerList.setAll(organizers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
