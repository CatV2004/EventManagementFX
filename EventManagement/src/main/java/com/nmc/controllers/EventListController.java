package com.nmc.controllers;

import com.nmc.pojo.Event;
import com.nmc.pojo.Venue;
import com.nmc.services.EventServices;
import com.nmc.services.VenueService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TableCell;
import javafx.stage.Modality;

public class EventListController implements Initializable {

    @FXML
    private TableView<Event> eventTable; // Định rõ kiểu dữ liệu của TableView
    @FXML
    private TableColumn<Event, Integer> colId;
    @FXML
    private TableColumn<Event, String> colName;
    @FXML
    private TableColumn<Event, Integer> colVenue;
    @FXML
    private TableColumn<Event, Timestamp> colStartTime;
    @FXML
    private TableColumn<Event, Timestamp> colEndTime;
    @FXML
    private TableColumn<Event, String> colStatus;
    @FXML
    private TextField searchField;

    private final EventServices eventServices = new EventServices();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadEvents(null);

        // Tự động điều chỉnh kích thước cột
        eventTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colId.prefWidthProperty().bind(eventTable.widthProperty().multiply(0.05)); // 5%
        colName.prefWidthProperty().bind(eventTable.widthProperty().multiply(0.25)); // 25%
        colVenue.prefWidthProperty().bind(eventTable.widthProperty().multiply(0.2)); // 20%
        colStartTime.prefWidthProperty().bind(eventTable.widthProperty().multiply(0.15)); // 15%
        colEndTime.prefWidthProperty().bind(eventTable.widthProperty().multiply(0.15)); // 15%
        colStatus.prefWidthProperty().bind(eventTable.widthProperty().multiply(0.1)); // 10%

        // Lắng nghe thay đổi trong ô tìm kiếm
        this.searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadEvents(newValue);
        });
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colVenue.setCellValueFactory(new PropertyValueFactory<>("venueId"));

        colVenue.setCellFactory(column -> new TableCell<Event, Integer>() {
            @Override
            protected void updateItem(Integer venueId, boolean empty) {
                super.updateItem(venueId, empty);
                if (empty || venueId == null) {
                    setText(null);
                } else {
                    // Lấy tên Venue từ ID (cần EventServices hoặc VenueService để lấy tên)
                    String venueName = getVenueNameById(venueId);
                    setText(venueName);
                }
            }
        });

        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

    }

    // Hàm lấy tên Venue từ ID
    private String getVenueNameById(int venueId) {
        try {
            Venue venue = VenueService.getVenueById(venueId);
            return venue != null ? venue.getName() : "Không xác định";
        } catch (Exception e) {
            return "Lỗi tải tên";
        }
    }

    private void loadEvents(String keyword) {
        try {
            List<Event> events;
            if (keyword == null || keyword.trim().isEmpty()) {
                events = eventServices.getEventsByTimeRange(null, null); // Lấy tất cả sự kiện
            } else {
                events = eventServices.getEventsByKeyword(keyword); // Tìm kiếm theo từ khóa
            }

            ObservableList<Event> eventList = FXCollections.observableArrayList(events);
            eventTable.setItems(eventList);
        } catch (SQLException ex) {
            Logger.getLogger(EventListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleAddEvent() {
        try {
            // Load FXML của form tạo sự kiện
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nmc/fxml/create_event.fxml"));
            Parent root = loader.load();

            // Tạo một cửa sổ mới (Stage)
            Stage stage = new Stage();
            stage.setTitle("Tạo sự kiện");
            stage.initModality(Modality.APPLICATION_MODAL); // Đảm bảo người dùng phải đóng trước khi quay lại trang chính
            stage.setScene(new Scene(root));

            // Hiển thị cửa sổ
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
