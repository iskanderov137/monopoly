package com.main;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class DiceHandler {

    private final Dice dice1;
    private final Dice dice2;
    private final Button btn;

    public DiceHandler(Dice dice1, Dice dice2, Button btn){
        this.dice1=dice1;
        this.dice2=dice2;
        this.btn=btn;
    }
    @FXML
    public void handleThrowButton(ActionEvent actionEvent) {
        dice1.throwDice();
        dice2.throwDice();
        btn.setDisable(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> btn.setDisable(false));
        pause.play();

    }
}
