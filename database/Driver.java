package com.example.database;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Driver extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sign.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Database");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
