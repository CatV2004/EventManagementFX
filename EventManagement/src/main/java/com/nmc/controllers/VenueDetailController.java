package com.nmc.controllers;

import com.nmc.pojo.Venue;
import com.nmc.services.VenueService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class VenueDetailController {

    @FXML
    private TextField txtId, txtName, txtLocation, txtCapacity;
    @FXML
    private Button btnSave, btnCancel;

    private Venue venue; // Đối tượng Venue hiện tại
    private VenueService venueService = new VenueService(); // Service thao tác với database

    public void setVenue(Venue venue) {
        this.venue = venue;
        if (venue != null) {
            txtId.setText(String.valueOf(venue.getId()));
            txtId.setEditable(false); // Không cho sửa ID
            txtName.setText(venue.getName());
            txtLocation.setText(venue.getLocation());
            txtCapacity.setText(String.valueOf(venue.getCapacity()));
        }
    }

    @FXML
    private void handleSave() {
        if (venue != null) {
            try {
                venue.setName(txtName.getText());
                venue.setLocation(txtLocation.getText());
                venue.setCapacity(Integer.parseInt(txtCapacity.getText()));

                if (venueService.updateVenue(venue)) {
                    showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công!", "Địa điểm đã được cập nhật.");
                    
                    VenueListController.loadVenuesFromDB();
                    
                    closeWindow();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại!", "Không thể cập nhật địa điểm.");
                }
            } catch (SQLException | NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi!", "Lỗi khi cập nhật địa điểm: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) txtId.getScene().getWindow();
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
