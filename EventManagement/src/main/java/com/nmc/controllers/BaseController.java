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
import javafx.scene.layout.BorderPane;

/**
 *
 * @author FPTSHOP
 */
public class BaseController {

    @FXML
    private BorderPane rootPane;

    @FXML
    public void initialize() {
        setRootPane(rootPane);
    }

    @FXML
    private void handleHome(ActionEvent event) {
        loadContent("/com/nmc/fxml/home_content.fxml");
    }

    @FXML
    private void handleMyEvents(ActionEvent event) {
        loadContent("/com/nmc/fxml/event_list.fxml");
    }

    @FXML
    private void handleVenue(ActionEvent event) {
        loadContent("/com/nmc/fxml/venue_list.fxml");
    }

    @FXML
    private void handleOrganizer(ActionEvent event) {
        loadContent("/com/nmc/fxml/organizer_list.fxml");
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

    public BorderPane getRootPane() {
        return rootPane;
    }

    public void setRootPane(BorderPane rootPane) {
        this.rootPane = rootPane;
    }

}
