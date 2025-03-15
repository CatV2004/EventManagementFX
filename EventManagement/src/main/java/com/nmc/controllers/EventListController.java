package com.nmc.controllers;

import com.nmc.pojo.Event;
import com.nmc.pojo.Organizer;
import com.nmc.pojo.Venue;
import com.nmc.services.EventServices;
import com.nmc.services.OrganizerService;
import com.nmc.services.VenueService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventListController implements Initializable {

    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event, Integer> colId;
    @FXML
    private TableColumn<Event, String> colName;
    @FXML
    private TableColumn<Event, Integer> colVenue;
    @FXML
    private TableColumn<Event, Integer> colOrganizer;
    @FXML
    private TableColumn<Event, Timestamp> colStartTime;
    @FXML
    private TableColumn<Event, Timestamp> colEndTime;
    @FXML
    private TableColumn<Event, String> colDes;
    @FXML
    private TableColumn<Event, String> colStatus;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnEdit, btnDelete;

    private final EventServices eventServices = new EventServices();
    private static final ObservableList<Event> eventList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadEvents(null);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> loadEvents(newVal));

        btnEdit.setDisable(true);
        btnDelete.setDisable(true);

        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
        });
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colVenue.setCellValueFactory(new PropertyValueFactory<>("venueId"));
        colOrganizer.setCellValueFactory(new PropertyValueFactory<>("organizerId")); // Đổi thành ID để lấy đối tượng sau
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colDes.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load venue name trực tiếp từ VenueService
        colVenue.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer venueId, boolean empty) {
                super.updateItem(venueId, empty);
                if (empty || venueId == null) {
                    setText(null);
                } else {
                    try {
                        Venue venue = new VenueService().getVenueById(venueId).orElse(null);
                        setText(venue != null ? venue.getName() : "Không xác định");
                    } catch (SQLException e) {
                        setText("Lỗi tải tên");
                        e.printStackTrace();
                    }
                }
            }
        });

        // Load organizer name trực tiếp từ OrganizerService
        colOrganizer.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer organizerId, boolean empty) {
                super.updateItem(organizerId, empty);
                if (empty || organizerId == null) {
                    setText(null);
                } else {
                    try {
                        Organizer organizer = new OrganizerService().getOrganizerById(organizerId).orElse(null);
                        setText(organizer != null ? organizer.getName() : "Không xác định");
                    } catch (SQLException e) {
                        setText("Lỗi tải tên");
                        e.printStackTrace();
                    }
                }
            }
        });

        eventTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadEvents(String keyword) {
        try {
            List<Event> events;
            if (keyword == null || keyword.trim().isEmpty()) {
                events = eventServices.getEventsByTimeRange(null, null);
            } else {
                events = eventServices.getEventsByKeyword(keyword);
            }

            eventList.setAll(events);
            eventTable.setItems(eventList);
        } catch (SQLException ex) {
            Logger.getLogger(VenueListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleAddEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nmc/fxml/create_event.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Thêm sự kiện");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleDeleteEvent() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn xóa sự kiện này không?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    if (eventServices.deleteEvent(selectedEvent.getId())) {
                        showAlert(Alert.AlertType.INFORMATION, "Xóa thành công!", "Sự kiện đã bị xóa.");
                        loadEvents(null); // Cập nhật lại danh sách trên giao diện
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Lỗi!", "Không thể xóa sự kiện.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi!", "Lỗi khi xóa sự kiện: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleEditEvent() throws SQLException {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            openEventDetailForm(selectedEvent);
        }
    }
    
    private void openEventDetailForm(Event event) throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nmc/fxml/event_detail.fxml"));
            Parent root = loader.load();

            EditEventController controller = loader.getController();
            controller.setEvent(event); // Truyền dữ liệu vào form

            Stage stage = new Stage();
            stage.setTitle("Chỉnh sửa sự kiện");
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
            EventServices eventService = new EventServices();
            List<Event> events = eventService.getEventsByTimeRange(null, null); // Hàm lấy dữ liệu từ database
            eventList.setAll(events); // Cập nhật lại danh sách
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
