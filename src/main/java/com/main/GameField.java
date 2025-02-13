package com.main;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

public class GameField {
    public Pane pn;

    public ArrayList<Circle> userCircles = new ArrayList<>();
    public ArrayList<Label> userBalancesLabels = new ArrayList<>();
    public ArrayList<Card> cards = new ArrayList<>();
    public ArrayList<Label> cardsLabels = new ArrayList<>();
    public ArrayList<Pane> cardsPanes = new ArrayList<>();

    private final Label timeLeftLabel = new Label();
    private final Label curPlayerLabel = new Label();
    private final Circle curUserCircle = new Circle();
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
            createCardPane(card);
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
        throwButton.setMinWidth(200);
        throwButton.setMinHeight(50);
        throwButton.setFont(new Font("Arial", 28));
        throwButton.setTranslateX(840);
        throwButton.setTranslateY(200);

        this.pn.getChildren().add(throwButton);
    }

    public void createCardPane(Card card) {
        Pane cardPane = new Pane();
        cardPane.setTranslateX(card.getPosX());
        cardPane.setTranslateY(card.getPosY());
        cardPane.setPrefSize(card.getWidth(), card.getHeight());
        cardsPanes.add(cardPane);

        BackgroundFill bgFill = new BackgroundFill(getGradient(card.borderColor,card), CornerRadii.EMPTY, null);
        cardPane.setBackground(new Background(bgFill));
        Label priceLabel = new Label("");
        cardsLabels.add(priceLabel);
        drawPriceLabelText(card);
        if (card.index > 0 && card.index < 10) {
            priceLabel.setRotate(90);
            priceLabel.setTranslateY((double) card.getHeight() / 4);
            priceLabel.setTranslateX((double) card.getWidth() / 2 + 4);
        } else if (card.index > 10 && card.index < 20) {
            priceLabel.setTranslateY((double) card.getHeight() / 2 + 12);
            priceLabel.setTranslateX((double) card.getWidth() / 4 - 4);
        } else if (card.index > 20 && card.index < 30) {
            priceLabel.setRotate(270);
            priceLabel.setTranslateY((double) card.getHeight() / 4);
        } else if (card.index > 30 && card.index < 50) {
            priceLabel.setTranslateX((double) card.getWidth() / 4 - 4);
        }
        priceLabel.setFont(new Font("Arial", 20));
        pn.getChildren().add(cardPane);
        cardPane.getChildren().add(priceLabel);
    }

    public void buyCard(User user,Card card) {
        card.setBelongs(user);
        card.borderColor = user.getColor();
        BackgroundFill bgFill = new BackgroundFill(getGradient(card.borderColor,card), CornerRadii.EMPTY, null);
        cardsPanes.get(card.index).setBackground(new Background(bgFill));
        card.setOccupied(true);
        drawPriceLabelText(card);
    }

    public void drawPriceLabelText(Card card){
        if (card.getType() == CardTypes.CARD_TYPE_PROPERTY || card.getType() == CardTypes.CARD_TYPE_SPECIAL_PROPERTY) {
            if (card.isOccupied()) {
                cardsLabels.get(card.index).setText(card.getRentPrice()+"$");
            } else {
                cardsLabels.get(card.index).setText(card.getPrice() +"$");
            }
            cardsLabels.get(card.index).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));
            cardsLabels.get(card.index).setPadding(new Insets(8));
        }
    }

    private LinearGradient getGradient(Color color, Card card) {
        Stop[] stops = new Stop[]{
                new Stop(0.0, Color.TRANSPARENT),
                new Stop(0.75, Color.TRANSPARENT),
                new Stop(1.0, color)
        };
        LinearGradient gradient = null;
        if (card.index < 10 && card.index > 0 && (card.getType() == CardTypes.CARD_TYPE_PROPERTY || card.getType() == CardTypes.CARD_TYPE_SPECIAL_PROPERTY)) {
            gradient = new LinearGradient(
                    0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops
            );
        } else if (card.index > 10 && card.index < 20 && (card.getType() == CardTypes.CARD_TYPE_PROPERTY || card.getType() == CardTypes.CARD_TYPE_SPECIAL_PROPERTY)) {
            gradient = new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops
            );
        } else if (card.index > 20 && card.index < 30 && (card.getType() == CardTypes.CARD_TYPE_PROPERTY || card.getType() == CardTypes.CARD_TYPE_SPECIAL_PROPERTY)) {
            gradient = new LinearGradient(
                    1, 0, 0, 0, true, CycleMethod.NO_CYCLE, stops
            );
        } else if (card.index > 30 && card.index < 40 && (card.getType() == CardTypes.CARD_TYPE_PROPERTY || card.getType() == CardTypes.CARD_TYPE_SPECIAL_PROPERTY)) {
            gradient = new LinearGradient(
                    0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stops
            );
        }
        return gradient;
    }

    public void createUsersBalancesLabel(Users users) {
        for (int i = 0; i < users.users.size(); i++) {
            Label lb = new Label();
            lb.setFont(new Font("Arial", 16));
            String text = "";
            if (Objects.equals(users.users.get(i).getColor().toString(), Color.RED.toString())) {
                text = "Red`s balance: ";
            } else if (Objects.equals(users.users.get(i).getColor().toString(), Color.BLUE.toString())) {
                text = "Blue`s balance: ";
            } else if (Objects.equals(users.users.get(i).getColor().toString(), Color.YELLOW.toString())) {
                text = "Yellow`s balance: ";
            }
            lb.setTextFill(Color.WHITE);
            lb.setText(text + users.users.get(i).getBalance());
            lb.setTranslateX(5);
            lb.setTranslateY((i * 50) + 25);
            userBalancesLabels.add(lb);
        }
        for (Label userBalancesLabel : userBalancesLabels) {
            this.pn.getChildren().add(userBalancesLabel);
        }
    }

    public void drawUsersBalances(Users users) {
        for (int i = 0; i < users.users.size(); i++) {
            String text = "";
            if (Objects.equals(users.users.get(i).getColor().toString(), Color.RED.toString())) {
                text = "Red`s balance: ";
            } else if (Objects.equals(users.users.get(i).getColor().toString(), Color.BLUE.toString())) {
                text = "Blue`s balance: ";
            } else if (Objects.equals(users.users.get(i).getColor().toString(), Color.YELLOW.toString())) {
                text = "Yellow`s balance: ";
            }
            userBalancesLabels.get(i).setText(text + users.users.get(i).getBalance());
        }
    }

    public void drawDice(Dice dice) {
        this.pn.getChildren().add(dice.group);
    }

    public void createCurPlayerLabel(Color color) {
        curPlayerLabel.setTranslateX(350);
        curPlayerLabel.setTranslateY(130);
        curPlayerLabel.setText("Player`s turn: ");
        curPlayerLabel.setTextFill(Color.BLACK);
        curPlayerLabel.setFont(new Font("Arial", 28));

        curUserCircle.setCenterX(550);
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

        Color currentColor = user.getColor();
        userCircles.get(indexUser).setFill(currentColor);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1.5), userCircles.get(indexUser));
        transition.setToX(x - userCircles.get(indexUser).getCenterX());
        transition.setToY(y - userCircles.get(indexUser).getCenterY());
        transition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        transition.setOnFinished(event -> {
            userCircles.get(indexUser).setCenterX(x);
            userCircles.get(indexUser).setCenterY(y);
            userCircles.get(indexUser).setTranslateX(0);
            userCircles.get(indexUser).setTranslateY(0);
            userCircles.get(indexUser).setFill(currentColor);
        });

        transition.play();
    }

    public void drawUsers(Users users) {
        for (int i = 0; i < users.users.size(); i++) {
            this.drawUser(users.users.get(i), i);
        }
    }

    private void loadCards() {
        this.cards.add(new Card(225, screenSizeY - 135, Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_START, 0, 0, false, 0, 0, 0, 0));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 290, 190, false, 1, 1, 29, 180));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 300, 200, false, 2, 1, 30, 190));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 500, 380, false, 3, 9, 280, 0));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 310, 190, false, 4, 1, 31, 200));
        this.cards.add(new Card(225, this.cards.get(this.cards.size() - 1).getPosY() - Card.CHANCE_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.CHANCE_HEIGHT,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 5, 11, 0, 0));
        this.cards.add(new Card(215, this.cards.get(this.cards.size() - 1).getPosY() - Card.CHANCE_HEIGHT - 5,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT + 5,
                CardTypes.CARD_TYPE_PROPERTY, 230, 130, false, 6, 2, 23, 120));
        this.cards.add(new Card(215, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT + 5,
                CardTypes.CARD_TYPE_PROPERTY, 250, 150, false, 7, 2, 25, 140));
        this.cards.add(new Card(215, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 350, 250, false, 8, 10, 35, 0));
        this.cards.add(new Card(215, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 2,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT + 5,
                CardTypes.CARD_TYPE_PROPERTY, 240, 140, false, 9, 2, 24, 130));
        this.cards.add(new Card(215, this.cards.get(this.cards.size() - 1).getPosY() - Card.PROPERTY_HEIGHT - 40,
                Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_CHARITY, 0, 0, false, 10, 0, 0, 0));
        // Up
        this.cards.add(new Card(215 + Card.PROPERTY_WIDTH + 2, this.cards.get(this.cards.size() - 1).getPosY(),
                Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 200, 100, false, 11, 3, 20, 90));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 220, 120, false, 12, 3, 22, 110));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 290, 200, false, 13, 10, 25, 0));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 210, 110, false, 14, 3, 21, 100));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.CHANCE_HEIGHT + 10, 115,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 15, 11, 0, 0));
        this.cards.add(new Card(Card.CHANCE_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 5,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 260, 160, false, 16, 4, 26, 150));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 280, 280, false, 17, 4, 28, 170));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 300, 180, false, 18, 9, 220, 0));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 270, 170, false, 19, 4, 27, 160));
        this.cards.add(new Card(Card.PROPERTY_HEIGHT + this.cards.get(this.cards.size() - 1).getPosX() + 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_JAIL, 0, 0, false, 20, 0, 0, 0));

        //Right
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 230, 130,
                Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 330, 230, false, 21, 5, 33, 320));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 230,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 320, 220, false, 22, 5, 32, 210));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 230,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 500, 380, false, 23, 9, 280, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 230,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 340, 240, false, 24, 5, 34, 230));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 225,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.CHANCE_HEIGHT,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 25, 11, 0, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.CHANCE_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 400, 300, false, 26, 6, 40, 260));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 380, 280, false, 27, 6, 38, 270));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 400, 300, false, 28, 12, 200, 0));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, Card.PROPERTY_HEIGHT,
                CardTypes.CARD_TYPE_PROPERTY, 390, 290, false, 29, 6, 39, 280));
        this.cards.add(new Card(screenSizeX - Card.PROPERTY_WIDTH - 220,
                this.cards.get(this.cards.size() - 1).getPosY() + Card.PROPERTY_HEIGHT + 2, Card.PROPERTY_WIDTH, 115,
                CardTypes.CARD_TYPE_TAX_OFFICE, 0, 0, false, 30, 0, 0, 0));

        //Down
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 430, 330, false, 31, 7, 43, 260));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 420, 320, false, 32, 7, 42, 310));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 500, 400, false, 33, 12, 300, 0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 410, 310, false, 34, 7, 41, 300));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.CHANCE_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.CHANCE_HEIGHT, 115,
                CardTypes.CARD_TYPE_CHANCE, 0, 0, false, 35, 11, 0, 0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 350, 270, false, 36, 8, 35, 240));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 370, 250, false, 37, 8, 37, 260));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_SPECIAL_PROPERTY, 450, 350, false, 38, 10, 45, 0));
        this.cards.add(new Card(this.cards.get(this.cards.size() - 1).getPosX() - Card.PROPERTY_HEIGHT - 2,
                this.cards.get(this.cards.size() - 1).getPosY(), Card.PROPERTY_HEIGHT, 115,
                CardTypes.CARD_TYPE_PROPERTY, 360, 260, false, 39, 8, 36, 250));
    }


}
