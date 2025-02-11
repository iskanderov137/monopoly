package com.main;

import com.main.interfaces.DiceRolledListener;
import com.main.interfaces.GameListener;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class DiceHandler {

    public final Dice dice1;
    public final Dice dice2;
    public final Button throwButton;
    private GameListener listener;

    public DiceHandler(GameListener listener){
        this.dice1 = new Dice(50, 250, 50, 350);
        this.dice2 = new Dice(300, 501, 50, 350);

        this.throwButton= new Button("Throw Dice");
        throwButton.setTranslateX(500);
        throwButton.setTranslateY(950);
        this.listener=listener;
    }
    @FXML
    public void handleThrowButton(ActionEvent actionEvent) {
        dice1.throwDice();
        dice2.throwDice();
        this.listener.onDiceRolled(dice1.value+dice2.value);
        throwButton.setDisable(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> throwButton.setDisable(false));
        pause.play();
    }
}
