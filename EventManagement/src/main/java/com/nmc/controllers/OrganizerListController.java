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
import com.nmc.services.OrganizerService;
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

public class OrganizerListController implements Initializable {

    @FXML
    private TableView<Organizer> organizerTable;
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

    private final OrganizerService organizerService = new OrganizerService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            List<Organizer> organizers = (keyword == null || keyword.trim().isEmpty())
                    ? organizerService.getAllOrganizers()
                    : organizerService.getOrganizersByKeyword(keyword);

            ObservableList<Organizer> organizerList = FXCollections.observableArrayList(organizers);
            organizerTable.setItems(organizerList);
        } catch (SQLException ex) {
            Logger.getLogger(OrganizerListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void handleAddOrganizer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nmc/fxml/organizer_form.fxml"));
            Parent organizerForm = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(organizerForm));
            stage.setTitle("Thêm tổ chức");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleEditOrganizer(ActionEvent event) {
        
    }
    
    @FXML
    private void handleDeleteOrganizer(ActionEvent event) {
        
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
