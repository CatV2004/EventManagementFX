package com.nmc.controllers;

import com.nmc.pojo.Organizer;
import com.nmc.services.OrganizerService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class OrganizerFormController {

    @FXML
    private TextField txtName, txtContactPerson, txtEmail, txtPhone;
    @FXML
    private Button btnSave, btnCancel;

    private final OrganizerService organizerService = new OrganizerService();

    @FXML
    private void handleSave() {
        String name = txtName.getText().trim();
        String contactPerson = txtContactPerson.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty() || contactPerson.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Lỗi nhập liệu", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        Organizer organizer = new Organizer(name, contactPerson, email, phone);
        try {
            if (organizerService.addOrganizer(organizer)) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Nhà tổ chức đã được thêm.");
                OrganizerListController.loadOrganizersFromDB(); // Cập nhật danh sách
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Thất bại", "Không thể thêm nhà tổ chức.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi thêm nhà tổ chức: " + e.getMessage());
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