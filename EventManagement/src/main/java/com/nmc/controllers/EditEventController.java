/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nmc.controllers;

import com.nmc.configs.DateUtils;
import com.nmc.pojo.Event;
import com.nmc.pojo.Organizer;
import com.nmc.pojo.Venue;
import com.nmc.services.EventServices;
import com.nmc.services.OrganizerService;
import com.nmc.services.VenueService;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

/**
 *
 * @author FPTSHOP
 */
public class EditEventController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadVenues();
        loadOrganizers();

        maxGuests.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 1));
    }

    @FXML
    private TextField eventId, eventName;
    @FXML
    private TextArea eventDescription;
    @FXML
    private ComboBox<Venue> venueSelection;
    @FXML
    private ComboBox<Organizer> organizerSelection;
    @FXML
    private Spinner<Integer> maxGuests;
    @FXML
    private DatePicker startDate, endDate;
    @FXML
    private Button btnSave, btnCancel;

    private Event event;
    private EventServices eventService = new EventServices(); // Service thao tác với database
    private VenueService venueService = new VenueService();
    private OrganizerService organizerService = new OrganizerService();

    private List<Venue> venues;
    private List<Organizer> organizers;

    public void setEvent(Event event) throws SQLException {
        this.event = event;
        if (event != null) {
            eventId.setText(String.valueOf(event.getId()));
            eventId.setEditable(false); // Không cho sửa ID
            eventName.setText(event.getName());
            eventDescription.setText(event.getDescription());

            // Hiển thị tên Venue của sự kiện
            try {
                Venue venue = new VenueService().getVenueById(event.getVenueId()).orElse(null);
                if (venue != null) {
                    venueSelection.setValue(venue);
                }

                // Hiển thị tên Organizer của sự kiện
                Organizer organizer = new OrganizerService().getOrganizerById(event.getOrganizerId()).orElse(null);
                if (organizer != null) {
                    organizerSelection.setValue(organizer);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            maxGuests.getValueFactory().setValue(event.getMaxGuests());
            startDate.setValue(DateUtils.dateToLocalDate(event.getStartTime()));
            endDate.setValue(DateUtils.dateToLocalDate(event.getEndTime()));
        }
    }

    @FXML
    private void handleUpdateEvent() {
        if (event != null) {
            try {
                event.setName(eventName.getText());
                event.setDescription(eventDescription.getText());

                Venue selectedVenue = venueSelection.getValue();
                Organizer selectedOrganizer = organizerSelection.getValue();

                if (selectedVenue == null || selectedOrganizer == null) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi!", "Vui lòng chọn địa điểm và tổ chức!");
                    return;
                }

                // Kiểm tra ID hợp lệ trước khi cập nhật
                if (selectedVenue.getId() <= 0 || selectedOrganizer.getId() <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi!", "ID địa điểm hoặc tổ chức không hợp lệ!");
                    return;
                }

                event.setVenueId(selectedVenue.getId());
                event.setOrganizerId(selectedOrganizer.getId());
                event.setMaxGuests(maxGuests.getValue());
                event.setStartTime(DateUtils.localDateToDate(startDate.getValue()));
                event.setEndTime(DateUtils.localDateToDate(endDate.getValue()));

                // Thực hiện cập nhật sự kiện
                if (eventService.updateEvent(event)) {
                    showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công!", "Sự kiện đã được cập nhật.");

                    EventListController.loadVenuesFromDB(); // Cập nhật danh sách sự kiện

                    closeWindow();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại!", "Không thể cập nhật sự kiện.");
                }
            } catch (SQLException | NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi!", "Lỗi khi cập nhật sự kiện: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) eventId.getScene().getWindow();
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
