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
        Dice dice=new Dice(50,250,50,350);
        Dice dice2=new Dice(300,501,50,350);

        Button throwButton = new Button("Throw Dice");
        throwButton.setTranslateX(500);
        throwButton.setTranslateY(950);
        DiceHandler diceHandler = new DiceHandler(dice,dice2,throwButton);
        throwButton.setOnAction(diceHandler::handleThrowButton);

        Scene scene = new Scene(new Group(dice.group,dice2.group,throwButton), 1000, 1000, true);
        scene.setFill(Color.LIGHTGRAY);
        PerspectiveCamera camera = new PerspectiveCamera(false);
        scene.setCamera(camera);

        primaryStage.setTitle("Monopoly");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}
