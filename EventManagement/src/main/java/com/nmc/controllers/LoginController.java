package com.nmc.controllers;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ToggleGroup roleToggleGroup;

    @FXML
    private RadioButton userRadio;

    @FXML
    private RadioButton adminRadio;

    private boolean authenticated = false;

    @FXML
    private void initialize() {
        // Bắt sự kiện nhấn Enter trên usernameField và passwordField
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
        
        roleToggleGroup = new ToggleGroup();
        userRadio.setToggleGroup(roleToggleGroup);
        adminRadio.setToggleGroup(roleToggleGroup);
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Giả lập kiểm tra đăng nhập (Thay thế bằng logic thực tế)
        if ("admin".equals(username) && "123".equals(password)) {
            // Lấy stage hiện tại và đóng nó
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
            authenticated = true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi đăng nhập");
            alert.setHeaderText("Tên đăng nhập hoặc mật khẩu không đúng!");
            alert.showAndWait();
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
    
    public String getSelectedRole() {
        if (userRadio.isSelected()) {
            return "Người dùng";
        } else if (adminRadio.isSelected()) {
            return "Admin";
        }
        return null;
    }

}
