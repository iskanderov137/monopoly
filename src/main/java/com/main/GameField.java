package com.main;

import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class GameField {
    public Pane pn;

    public ArrayList<Circle> userCircles = new ArrayList<>();
    public ArrayList<Label> userBalancesLabels = new ArrayList<>();
    public ArrayList<Card> cards = new ArrayList<>();

    private Label timeLeftLabel = new Label();
    private Label curPlayerLabel = new Label();
    private Circle curUserCircle = new Circle();
    private final Image bgImage = new Image(String.valueOf(Main.class.getResource("/imgs/bg.jpg")));
    public Button throwButton;

    public static int screenSizeX = 1400;
    public static int screenSizeY = 1000;
    public int maxTimePerTurn = 30000;

    public GameField() {
        this.pn = new Pane();
        for (int i = 0; i < 3; i++) {
            Circle circle = new Circle();
            circle.setRadius(20);
            userCircles.add(circle);
            pn.getChildren().add(circle);
        }

        this.createScene();
    }

    private void createScene() {
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,
                        true, true, true, false)
        );
        pn.setBackground(new Background(backgroundImage));
        pn.setPickOnBounds(false);

        createTimerLabel();

        this.loadCards();
        for (Card card : cards) {
            this.pn.getChildren().add(card.pn);
        }
        drawThrowButton();

        Rectangle blackBackground = new Rectangle(screenSizeX, screenSizeY, Color.BLACK);

        StackPane root = new StackPane();
        root.setPickOnBounds(false);
        root.getChildren().addAll(blackBackground, pn);

        Stage primaryStage = new Stage();
        Scene scene = new Scene(root, screenSizeX, screenSizeY, true);
        PerspectiveCamera camera = new PerspectiveCamera(false);
        scene.setCamera(camera);
        primaryStage.setTitle("Monopoly");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void drawThrowButton() {
        this.throwButton = new Button("Throw Dice");
        throwButton.setTranslateX(500);
        throwButton.setTranslateY(950);

        this.pn.getChildren().add(throwButton);
    }

    public void createUsersBalancesLabel(Users users){
        for(int i=0;i<users.users.size();i++){
            Label lb=new Label();
            lb.setFont(new Font("Arial",16));
            String text= "";
            if(Objects.equals(users.users.get(i).getColor().toString(), Color.RED.toString())){
                text="Red`s balance: ";
            } else if (Objects.equals(users.users.get(i).getColor().toString(), Color.BLUE.toString())){
                text="Blue`s balance: ";
            } else if (Objects.equals(users.users.get(i).getColor().toString(), Color.YELLOW.toString())){
                text="Yellow`s balance: ";
            }
            lb.setTextFill(Color.WHITE);
            lb.setText(text+ users.users.get(i).getBalance());
            lb.setTranslateX(5);
            lb.setTranslateY((i*50)+25);
            userBalancesLabels.add(lb);
        }
        for (Label userBalancesLabel : userBalancesLabels) {
            this.pn.getChildren().add(userBalancesLabel);
        }
    }

    public void drawUsersBalances(Users users){
        for (int i=0;i<users.users.size();i++) {
            String text= "";
            if(Objects.equals(users.users.get(i).getColor().toString(), Color.RED.toString())){
                text="Red`s balance: ";
            } else if (Objects.equals(users.users.get(i).getColor().toString(), Color.BLUE.toString())){
                text="Blue`s balance: ";
            } else if (Objects.equals(users.users.get(i).getColor().toString(), Color.YELLOW.toString())){
                text="Yellow`s balance: ";
            }
            userBalancesLabels.get(i).setText(text+ users.users.get(i).getBalance());
        }
    }

    public void drawDice(Dice dice) {
        this.pn.getChildren().add(dice.group);
    }

    public void createCurPlayerLabel(Color color) {
        curPlayerLabel.setTranslateX(150);
        curPlayerLabel.setTranslateY(130);
        curPlayerLabel.setText("Player`s turn: ");
        curPlayerLabel.setTextFill(Color.BLACK);
        curPlayerLabel.setFont(new Font("Arial", 28));

        curUserCircle.setCenterX(350);
        curUserCircle.setCenterY(150);
        curUserCircle.setFill(color);
        curUserCircle.setRadius(20);

        this.pn.getChildren().add(curPlayerLabel);
        this.pn.getChildren().add(curUserCircle);
    }

    public void drawCurPlayerLabelCircle(Color color) {
        curUserCircle.setFill(color);
    }

    public void createTimerLabel() {
        timeLeftLabel.setTranslateX(600);
        timeLeftLabel.setTranslateY(130);
        timeLeftLabel.setTextFill(Color.BLACK);
        timeLeftLabel.setFont(new Font("Arial", 40));
        this.pn.getChildren().add(timeLeftLabel);
        this.drawTimerLeft(maxTimePerTurn);
    }

    public void drawTimerLeft(long time) {
        timeLeftLabel.setText("Time left: " + time / 1000);
    }

    public void drawUser(User user, int indexUser) {
        int index = user.getPosition();
        Card card = this.cards.get(index);

        int x = ((card.getPosX() + card.getWidth() / 2) + indexUser * 25) - 25;
        int y = (card.getPosY() + card.getHeight() / 2);

        userCircles.get(indexUser).setCenterX(x);
        userCircles.get(indexUser).setCenterY(y);
        userCircles.get(indexUser).setFill(user.getColor());
    }

    public void drawUsers(Users users) {
        for (int i = 0; i < users.users.size(); i++) {
            this.drawUser(users.users.get(i), i);
        }
    }

    private void loadCards() {
        this.cards.add(new Card(225, screenSizeY - 135, Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_START, 0, 0, false, 0, 0,0));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 290, 190, false, 1, 1,29));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 300, 200, false, 2, 1,30));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 500, 380, false, 3, 9,280));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 310, 190, false, 4, 1,31));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.CHANCE_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.CHANCE_HEIGHT,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 5, 11,0));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.CHANCE_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 230, 130, false, 6, 2,23));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 250, 150, false, 7, 2,25));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 350, 250, false, 8, 10,35));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 240, 140, false, 9, 2,24));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 40,
                Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_CHARITY, 0, 0, false, 10, 0,0));
        // Up
        this.cards.add(new Card(225 + Card.PROPERTY_WIDTH + 2, this.cards.get(this.cards.size() - 1).getPosY(),
                Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 200, 100, false, 11, 3,20));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 220, 120, false, 12, 3,22));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 290, 200, false, 13, 10,25));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 210, 110, false, 14, 3,21));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.CHANCE_HEIGHT, 115,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 15, 11,0));
        this.cards.add(new Card(Card.CHANCE_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 260, 160, false, 16, 4,26));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 280, 280, false, 17, 4,28));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 300, 180, false, 18, 9,220));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 270, 170, false, 19, 4,27));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_JAIL, 0, 0, false, 20, 0,0));

        //Right
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220, 130,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 330, 230, false, 21, 5,33));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 320, 220, false, 22, 5,32));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 500, 380, false, 23, 9,280));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 340, 240, false, 24, 5,34));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.CHANCE_HEIGHT,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 25, 11,0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.CHANCE_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 400, 300, false, 26, 6,40));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 380, 280, false, 27, 6,38));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 400, 300, false, 28, 12,200));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 390, 290, false, 29, 6,39));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_TAX_OFFICE, 0, 0, false, 30, 0,0));

        //Down
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 430, 330, false, 31, 7,43));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 420, 320, false, 32, 7,42));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 500, 400, false, 33, 12,300));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 410, 310, false, 34, 7,41));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.CHANCE_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.CHANCE_HEIGHT, 115,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 35, 11,0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 350, 270, false, 36, 8,35));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 370, 250, false, 37, 8,37));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 450, 350, false, 38, 10,45));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 360, 260, false, 39, 8,36));
    }


}
