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
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author FPTSHOP
 */
public class OrganizerDetailController {

    @FXML
    private TextField txtId, txtName, txtContactPerson, txtEmail, txtPhone;
    @FXML
    private Button btnSave, btnCancel;

    private Organizer organizer;
    private OrganizerService organizerService = new OrganizerService();

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
        if (organizer != null) {
            txtId.setText(String.valueOf(organizer.getId()));
            txtId.setEditable(false); // Không cho sửa ID
            txtName.setText(organizer.getName());
            txtContactPerson.setText(organizer.getContactPerson());
            txtEmail.setText(organizer.getEmail());
            txtPhone.setText(organizer.getPhone());
        }
    }

    @FXML
    private void handleSave() {
        if (organizer != null) {
            try {
                organizer.setName(txtName.getText());
                organizer.setContactPerson(txtContactPerson.getText());
                organizer.setEmail(txtEmail.getText());
                organizer.setPhone(txtPhone.getText());

                if (organizerService.updateOrganizer(organizer)) {
                    showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công!", "Khách hàng đã được cập nhật.");

                    OrganizerListController.loadOrganizersFromDB();

                    closeWindow();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại!", "Không thể cập nhật khách hàng.");
                }
            } catch (SQLException | NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi!", "Lỗi khi cập nhật khách hàng: " + e.getMessage());
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
