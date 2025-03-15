package com.nmc.controllers;

import com.nmc.pojo.Venue;
import com.nmc.services.VenueService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class VenueFormController {

    @FXML
    private TextField txtName, txtLocation, txtCapacity;
    @FXML
    private Button btnSave, btnCancel;

    private final VenueService venueService = new VenueService();

    @FXML
    private void handleSave() {
        String name = txtName.getText().trim();
        String location = txtLocation.getText().trim();
        String capacityText = txtCapacity.getText().trim();

        if (name.isEmpty() || location.isEmpty() || capacityText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Lỗi nhập liệu", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        try {
            int capacity = Integer.parseInt(capacityText);
            Venue venue = new Venue(name, location, capacity); // ID tự tăng
            if (venueService.addVenue(venue)) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Địa điểm đã được thêm.");
                VenueListController.loadVenuesFromDB(); // Cập nhật danh sách
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Thất bại", "Không thể thêm địa điểm.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Sức chứa phải là số.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi thêm địa điểm: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
