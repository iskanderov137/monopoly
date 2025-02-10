package com.main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.PerspectiveCamera;
import javafx.scene.paint.Color;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Game game= new Game();

        primaryStage.setTitle("Monopoly");
        primaryStage.setScene(game.scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}
