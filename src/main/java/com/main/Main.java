package com.main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

public class Main extends Application {
    private long lastUpdate = 0;
    public long curTime = 0;

    public Dice dice1;
    public Dice dice2;

    public Users users;

    public HashMap<Integer, String> chances = new HashMap<>();

    private GameField gameField;

    @Override
    public void start(Stage primaryStage) {
        loadChances();
        curTime = Calendar.getInstance().getTime().getTime();

        this.dice1 = new Dice(50, 250, 50, 350);
        this.dice2 = new Dice(300, 501, 50, 350);

        users = new Users();
        gameField = new GameField();
        gameField.throwButton.setOnAction(this::handleThrowButton);
        gameField.drawUsers(users);
        gameField.createCurPlayerLabel(users.getCurUser().getColor());
        gameField.drawDice(dice1);
        gameField.drawDice(dice2);
        gameField.createUsersBalancesLabel(users);
//        buyButton.setOnAction(e -> buyCard());


//        DiceHandler diceHandler = new DiceHandler(this);
//        diceHandler.throwButton.setOnAction(diceHandler::handleThrowButton);
//        pn.getChildren().addAll(
//                diceHandler.throwButton, buyButton);
//                this.users.getUserByIndex(0).circle,this.users.getUserByIndex(1).circle,this.users.getUserByIndex(2).circle);

//        this.scene = new Scene(gameField.draw(), screenSizeX, screenSizeY, true);
//        PerspectiveCamera camera = new PerspectiveCamera(false);
//        scene.setCamera(camera);
//        primaryStage.setTitle("Monopoly");
//        primaryStage.setScene(scene);
//        primaryStage.show();
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate > 0) {
                    double deltaTime = (now - lastUpdate) / 1e9;
                    long timeLeft = gameField.maxTimePerTurn - Calendar.getInstance().getTime().getTime() + curTime;
                    gameField.drawTimerLeft(timeLeft);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {

                    }
                    if (Calendar.getInstance().getTime().getTime() - curTime > gameField.maxTimePerTurn) {
//                        users.eliminateUser();
//                        users.changeTurn();
//                        gameField.drawCurPlayerLabelCircle(users.getCurUser().getColor());
//                        curTime = Calendar.getInstance().getTime().getTime();
                    }
                }
                lastUpdate = now;
            }
        };
        gameLoop.start();
    }

    public static void main(String[] args) {
        launch();
    }

    @FXML
    public void handleThrowButton(ActionEvent actionEvent) {
        //Вивести кубики
        dice1.throwDice();
        dice2.throwDice();
        //Отримати суму очок
        int res = dice1.value + dice2.value;
        curTime = Calendar.getInstance().getTime().getTime();
        //Перемістити й перемалювати фішку поточного користувача на відповідну кількість ходів
        if (this.users.getCurUser().getPosition() + res > 40) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + 200);
            this.gameField.drawUsersBalances(users);
        }
        this.users.changeUserPosition(res);
        this.gameField.drawUsers(this.users);
        CardTypes typeOfCard = this.gameField.cards.get(this.users.getCurUser().getPosition()).getType();
        //Якщо попав на спеціальне поле виконати відповідну дію
        if (typeOfCard == CardTypes.CARD_TYPE_START) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + 200);
            this.gameField.drawUsersBalances(users);
        } else if (typeOfCard == CardTypes.CARD_TYPE_TAX_OFFICE) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 100);
            this.gameField.drawUsersBalances(users);
        } else if (typeOfCard == CardTypes.CARD_TYPE_JAIL) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 200);
            this.gameField.drawUsersBalances(users);
        } else if (typeOfCard == CardTypes.CARD_TYPE_CHARITY) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + 100);
            this.gameField.drawUsersBalances(users);
        } else if (typeOfCard == CardTypes.CARD_TYPE_PROPERTY) {
            Card card = this.gameField.cards.get(this.users.getCurUser().getPosition());
            if (card.isOccupied()) {
                this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - card.rentPrice);
            } else {
                // При недостатньому балансі для покупки продати найдешевшу карточку
                if (this.users.getCurUser().getBalance() >= card.getPrice()) {
                    //Можливо при можливості купування додати 30 сек до таймера
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Would you like to buy property?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        this.users.getCurUser().setBalance(users.getCurUser().getBalance() - card.getPrice());
                        card.buyCard(this.users.getCurUser());
                        //Додати у стек подій покупку
                        // Додати прослуховувач події для відʼємного балансу
                    }
                }
            }
        } else if (typeOfCard == CardTypes.CARD_TYPE_CHANCE) {
            generateChanceCard();
        }
        this.gameField.drawUsersBalances(users);
        this.users.changeTurn();
        curTime = Calendar.getInstance().getTime().getTime();
        this.gameField.drawCurPlayerLabelCircle(this.users.getCurUser().getColor());
    }

    public void loadChances() {
        chances.put(1, "Day of independence, get 10$ for every card you have.");
        chances.put(2, "Bank has made a mistake in your favor. Get 150$.");
        chances.put(3, "Fine for speeding. Pay 10$.");
        chances.put(4, "You have gained heritage. Get 500$.");
        chances.put(5, "Move three cards back.");
        chances.put(6, "Pay for insurance 50$.");
        chances.put(7, "Move to the Start and get 200$");
        chances.put(8, "You have earned percentage of paid taxes. Get 120$.");
        chances.put(9, "Move to the prison.");
        chances.put(10, "You have earned dividends. Get 100$");
        chances.put(11, "Visit to the doctor. Pay 80$");
        chances.put(12, "Repair of you buildings. Pay 40$ for every card you have.");
        chances.put(13, "Today is your birthday, every player gives you 20$.");
        chances.put(14, "Sell of shares. You get 100$.");
        chances.put(15, "Fine for speeding. Pay 20$.");
    }

    public void generateChanceCard() {
        Random random = new Random();
        int chance = random.nextInt(1, 16);
        if (chance == 1) {
            int numberOfCards = this.users.getCurUser().boughtCards.size();
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + numberOfCards * 10);
        } else if (chance == 2) {
            this.users.getCurUser().setBalance(150);
        } else if (chance == 3) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 10);
        } else if (chance == 4) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + 500);
        } else if (chance == 5) {
            this.users.changeUserPosition(40 - 3);
        } else if (chance == 6) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 50);
        } else if (chance == 7) {
            this.users.changeUserPosition(40 - this.users.getCurUser().getPosition());
        } else if (chance == 8) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + 120);
        } else if (chance == 9) {
            if (this.users.getCurUser().getPosition() > 20) {
                this.users.changeUserPosition(40 + (40 - 20 - this.users.getCurUser().getPosition()));
                this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 200);
            } else {
                this.users.changeUserPosition(40 + (40 - 20 - this.users.getCurUser().getPosition()));
                this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 200);
            }
        } else if (chance == 10) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + 100);
        } else if (chance == 11) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 80);
        } else if (chance == 12) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 40 * this.users.getCurUser().boughtCards.size());
        } else if (chance == 13) {
            for (User user : this.users.users) {
                user.setBalance(user.getBalance() - 20);
            }
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + 20 * this.users.users.size());
        } else if (chance == 14) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + 100);
        } else {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 20);
        }
        String generatedEvent = chances.get(chance);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(generatedEvent);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK");
        } else {
            System.out.println("OK");
        }
        gameField.drawUsers(users);
    }


}
