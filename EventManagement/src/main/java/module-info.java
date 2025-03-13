module com.nmc.eventmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; 
    
    opens com.nmc.pojo to javafx.base;
    opens com.nmc.controllers to javafx.fxml;
    opens com.nmc.eventmanagement to javafx.fxml;
    
    exports com.nmc.eventmanagement;
    exports com.nmc.controllers;

}
