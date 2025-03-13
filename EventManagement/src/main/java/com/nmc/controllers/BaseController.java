/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nmc.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author FPTSHOP
 */
public class BaseController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private VBox sidebar;

    @FXML
    private Button btnHome, btnMyEvents, btnVenue, btnOrganizer;

    private Button currentSelectedButton;

    @FXML
    public void initialize() {
        setRootPane(rootPane);
        
        if (btnHome != null) {
            handleHome(new ActionEvent(btnHome, null)); // Tải trang Home mặc định
        }
    }

    @FXML
    private void handleHome(ActionEvent event) {
        loadContent("/com/nmc/fxml/home_content.fxml");
        setActiveButton((Button) event.getSource());
    }

    @FXML
    private void handleMyEvents(ActionEvent event) {
        loadContent("/com/nmc/fxml/event_list.fxml");
        setActiveButton((Button) event.getSource());
    }

    @FXML
    private void handleVenue(ActionEvent event) {
        loadContent("/com/nmc/fxml/venue_list.fxml");
        setActiveButton((Button) event.getSource());
    }

    @FXML
    private void handleOrganizer(ActionEvent event) {
        loadContent("/com/nmc/fxml/organizer_list.fxml");
        setActiveButton((Button) event.getSource());
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadContent("/com/nmc/fxml/home.fxml");
    }

    protected void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newContent = loader.load();

            // Lấy controller của trang mới
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setRootPane(rootPane);  // Truyền rootPane cho controller mới
            }

            if (rootPane == null) {
                System.out.println("rootPane bị null!");  // Debug lỗi
                return;
            }

            rootPane.setCenter(newContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button button) {
        if (currentSelectedButton != null) {
            currentSelectedButton.getStyleClass().remove("selected");
        }
        button.getStyleClass().add("selected");
        currentSelectedButton = button;
    }

    public BorderPane getRootPane() {
        return rootPane;
    }

    public void setRootPane(BorderPane rootPane) {
        this.rootPane = rootPane;
    }

}
