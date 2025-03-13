/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nmc.controllers;

import com.nmc.pojo.Organizer;
import com.nmc.pojo.Venue;
import com.nmc.services.OrganizerService;
import com.nmc.services.VenueService;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author FPTSHOP
 */
public class CreateEventController {

    @FXML
    private Spinner maxGuests;

    @FXML
    private ComboBox<String> statusSelection;

    @FXML
    private ComboBox<Venue> venueSelection;

    @FXML
    private ComboBox<Organizer> organizerSelection;

    private final VenueService venueService = new VenueService();
    private final OrganizerService organizerService = new OrganizerService();

    public void initialize() {
        // Cấu hình Spinner cho phép nhập số
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1); // Min: 1, Max: 1000, Default: 1
        maxGuests.setValueFactory(valueFactory);
        maxGuests.setEditable(true);

        // Gọi phương thức nạp dữ liệu vào ComboBox
        loadVenues();
        loadOrganizers();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        // Lấy cửa sổ (stage) hiện tại và đóng nó
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    private void loadVenues() {
        try {
            List<Venue> venues = venueService.getAllVenues();
            if (venues.isEmpty()) {
                System.out.println("Không có dữ liệu địa điểm nào được tải.");
            }
            ObservableList<Venue> venueList = FXCollections.observableArrayList(venues);
            venueSelection.setItems(venueList);

        } catch (SQLException e) {
            System.err.println("Lỗi khi tải danh sách địa điểm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadOrganizers() {
        try {
            List<Organizer> organizers = organizerService.getAllOrganizers();
            ObservableList<Organizer> organizerList = FXCollections.observableArrayList(organizers);
            organizerSelection.setItems(organizerList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddVenue() {
        System.out.println("Mở form thêm địa điểm mới...");
        // Hiển thị một cửa sổ nhập địa điểm mới
    }

    @FXML
    private void handleAddOrganizer() {
        System.out.println("Mở form thêm nhà tổ chức mới...");
        // Hiển thị một cửa sổ nhập nhà tổ chức mới
    }

}
