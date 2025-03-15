package com.nmc.controllers;

import com.nmc.configs.DateUtils;
import com.nmc.pojo.Event;
import com.nmc.pojo.Organizer;
import com.nmc.pojo.Venue;
import com.nmc.services.EventServices;
import com.nmc.services.OrganizerService;
import com.nmc.services.VenueService;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class CreateEventController {

    public void initialize() {
        maxGuests.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 10));
        maxGuests.setEditable(true);
        loadVenues();
        loadOrganizers();
    }
    @FXML
    private TextField eventName;
    @FXML
    private TextArea eventDescription;
    @FXML
    private Spinner<Integer> maxGuests;
    @FXML
    private DatePicker startDate, endDate;
    @FXML
    private ComboBox<Venue> venueSelection;
    @FXML
    private ComboBox<Organizer> organizerSelection;
    @FXML
    private Button btnSave, btnCancel;

    private final EventServices eventService = new EventServices();
    private final VenueService venueService = new VenueService();
    private final OrganizerService organizerService = new OrganizerService();

    @FXML
    private void handleSaveEvent() {
        try {
            // Lấy dữ liệu từ form
            String name = eventName.getText().trim();
            String description = eventDescription.getText().trim();
            Venue venue = venueSelection.getValue();
            Organizer organizer = organizerSelection.getValue();
            int maxGuestCount = maxGuests.getValue();
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();

            // Kiểm tra dữ liệu hợp lệ
            if (name.isEmpty() || description.isEmpty() || venue == null || organizer == null || start == null || end == null) {
                showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            if (end.isBefore(start)) {
                showAlert(Alert.AlertType.ERROR, "Lỗi ngày", "Ngày kết thúc không thể trước ngày bắt đầu!");
                return;
            }

            // Tạo đối tượng Event mới
            Event newEvent = new Event();
            newEvent.setName(name);
            newEvent.setDescription(description);
            newEvent.setVenueId(venue.getId());
            newEvent.setOrganizerId(organizer.getId());
            newEvent.setMaxGuests(maxGuestCount);
            newEvent.setStartTime(DateUtils.localDateToDate(start));
            newEvent.setEndTime(DateUtils.localDateToDate(end));

            // Lưu vào database
            if (eventService.addEvent(newEvent)) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Sự kiện đã được thêm!");

                EventListController.loadVenuesFromDB();

                // Đóng cửa sổ
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm sự kiện!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi lưu sự kiện: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) eventName.getScene().getWindow();
        stage.close();
    }

    private void loadVenues() {
        try {
            List<Venue> venues = venueService.getAllVenues();
            venueSelection.setItems(FXCollections.observableArrayList(venues));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadOrganizers() {
        try {
            List<Organizer> organizers = organizerService.getAllOrganizers();
            organizerSelection.setItems(FXCollections.observableArrayList(organizers));
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
