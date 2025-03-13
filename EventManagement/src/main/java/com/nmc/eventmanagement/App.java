package com.nmc.eventmanagement;

import com.nmc.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.stage.Modality;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage; // Lưu lại stage chính để sử dụng sau

        // Tạo màn hình Login
        Stage loginStage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/nmc/fxml/login.fxml"));
        Scene loginScene = new Scene(loader.load());

        LoginController loginController = loader.getController(); // Lấy controller của form login
        loginStage.setScene(loginScene);
        loginStage.setTitle("Login");
        loginStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với Home
        loginStage.setResizable(false);

        // Hiển thị form đăng nhập và chờ người dùng nhập
        loginStage.showAndWait();

        // Kiểm tra nếu đăng nhập thành công thì mới vào Home
        if (loginController.isAuthenticated()) {
            scene = new Scene(loadFXML("home"));
            primaryStage.setScene(scene);
            primaryStage.setTitle("Home");

            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
            primaryStage.setMaximized(true);
            
            primaryStage.show();
        } else {
            System.out.println("Người dùng chưa đăng nhập. Thoát chương trình.");
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/nmc/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
