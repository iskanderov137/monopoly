package com.main;

import com.main.interfaces.DiceRolledListener;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Game implements DiceRolledListener {

    public List<User> users = new ArrayList<>();
    public List<Card> cards = new ArrayList<>();

    public int screenSizeX = 1000;
    public int screenSizeY = 1000;

    public int curUser;

    public Scene scene;

    public Game() {
        this.loadCards();

        this.curUser = 0;
        users.add(new User(Color.RED, 50, 900));
        users.add(new User(Color.YELLOW, 100, 900));

        DiceHandler diceHandler = new DiceHandler(this);
        diceHandler.throwButton.setOnAction(diceHandler::handleThrowButton);

        Pane pn = new Pane();
        Image bgImage = new Image(String.valueOf(Game.class.getResource("/imgs/bg.jpg")));
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,
                        true, true, true, false)
        );
        pn.setBackground(new Background(backgroundImage));
        pn.setPickOnBounds(false);

        for (Card card : cards) {
            pn.getChildren().add(card.pn);
        }

        pn.getChildren().addAll(diceHandler.dice1.group, diceHandler.dice2.group,
                diceHandler.throwButton, users.get(0).circle,users.get(1).circle);

        this.scene = new Scene(pn, screenSizeX, screenSizeY, true);
        PerspectiveCamera camera = new PerspectiveCamera(false);
        scene.setCamera(camera);
    }

    @Override
    public void onDiceRolled(int total) {
        users.get(curUser).setPosition(users.get(curUser).getPosition() + total);
        int numberOfUsersOnCard=0;
        for(User user : users){
            if(user.getPosition() == users.get(curUser).getPosition()){
                numberOfUsersOnCard++;
            }
        }
        for (int i=0;i<cards.size();i++) {
            if (i == users.get(curUser).getPosition()) {
                if(numberOfUsersOnCard <2){
                    users.get(curUser).setPosX(cards.get(i).getPosX()+50*numberOfUsersOnCard);
                    users.get(curUser).setPosY(cards.get(i).getPosY()+50);
                }
                else{
                    users.get(curUser).setPosX(cards.get(i).getPosX()+50*numberOfUsersOnCard);
                    users.get(curUser).setPosY(cards.get(i).getPosY()+50*numberOfUsersOnCard);
                }
            }
        }
        curUser = (curUser + 1) % users.size();
    }

    private void loadCards(){
        this.cards.add(new Card(25, screenSizeY - 135, Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_START, 0, 0, false, 0));
        this.cards.add(new Card(25, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(25, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(25, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(25, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(25, this.cards.get(this.cards.size() - 1).getPosY() - Card.CHANCE_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.CHANCE_HEIGHT,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 0));
        this.cards.add(new Card(25, this.cards.get(this.cards.size() - 1).getPosY() - Card.CHANCE_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(25, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(25, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(25, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(25, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 40,
                Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_CHARITY, 0, 0, false, 0));

        // Up
        this.cards.add(new Card(25 + Card.PROPERTY_WIDTH + 2, this.cards.get(this.cards.size() - 1).getPosY(),
                Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.CHANCE_HEIGHT, 115,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 0));
        this.cards.add(new Card(Card.CHANCE_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_JAIL, 0, 0, false, 0));

        //Right
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 20, 130,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 20,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 20,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 20,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 20,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.CHANCE_HEIGHT,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 20,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.CHANCE_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 20,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 20,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 20,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 20,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_TAX_OFFICE, 0, 0, false, 0));

        //Down
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.CHANCE_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.CHANCE_HEIGHT, 115,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 0, 0, false, 0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 0, 0, false, 0));
    }

}
