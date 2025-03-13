package com.nmc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HomeController extends BaseController {

    @FXML
    private void handleCreateEvent(ActionEvent event) {
        loadContent("/com/nmc/fxml/event_list.fxml");
    }

}
