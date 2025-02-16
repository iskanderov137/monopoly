package com.main;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
//Головний движок гри
public class Main extends Application {
    //Кадр останнього оновлення екрана
    private long lastUpdate = 0;
    //Поточний час (Для таймера)
    public long curTime = 0;
    //Створення двох кубиків
    public Dice dice1;
    public Dice dice2;
    //Сповіщення про виграш
    private final Alert winAlert = new Alert(Alert.AlertType.CONFIRMATION);
    //Список усіх гравців
    public Users users;
    //Список усіх випадкових карточок
    public HashMap<Integer, String> chances = new HashMap<>();
    //Ігрове поле
    private GameField gameField;
    //Метод що розпочинає гру
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

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate > 0) {
                    long timeLeft = gameField.maxTimePerTurn - Calendar.getInstance().getTime().getTime() + curTime;
                    gameField.drawTimerLeft(timeLeft);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ignored) {
                    }
                    if (Calendar.getInstance().getTime().getTime() - curTime > gameField.maxTimePerTurn) {
                        users.eliminateUser();
                        if (-1 == users.changeTurn()) {
                            Platform.runLater(() -> {
                                winAlert.setTitle("Winning");
                                winAlert.setHeaderText("You won!");
                                if(!winAlert.isShowing()) {
                                    winAlert.showAndWait();
                                }
                                System.exit(0);
                            });
                        }
                        gameField.drawCurPlayerLabelCircle(users.getCurUser().getColor());
                        curTime = Calendar.getInstance().getTime().getTime();

                    }
                }
                lastUpdate = now;
            }
        };
        gameLoop.start();
    }
    //Головний метод для запуску програми
    public static void main(String[] args) {
        launch();
    }
    //Метод для обробки кидання кубиків+ логіка з покупками
    @FXML
    public void handleThrowButton(ActionEvent actionEvent) {
        //Вивести кубики
        dice1.throwDice();
        dice2.throwDice();
        //Отримати суму очок
        int res = dice1.value + dice2.value;
        gameField.throwButton.setDisable(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> gameField.throwButton.setDisable(false));
        pause.play();
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Start");
            alert.setHeaderText("You have received 200$!");
            alert.showAndWait();
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + 200);
            this.gameField.drawUsersBalances(users);
        } else if (typeOfCard == CardTypes.CARD_TYPE_TAX_OFFICE) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 100);
            this.gameField.drawUsersBalances(users);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Tax office");
            alert.setHeaderText("You have been fined for 100$!");
            alert.showAndWait();
        } else if (typeOfCard == CardTypes.CARD_TYPE_JAIL) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Jail");
            alert.setHeaderText("You have been fined for 200$!");

            alert.showAndWait();
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 200);
            this.gameField.drawUsersBalances(users);
        } else if (typeOfCard == CardTypes.CARD_TYPE_CHARITY) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Charity");
            alert.setHeaderText("You have received 100$!");

            alert.showAndWait();
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + 100);
            this.gameField.drawUsersBalances(users);
        } else if (typeOfCard == CardTypes.CARD_TYPE_PROPERTY) {
            Card card = this.gameField.cards.get(this.users.getCurUser().getPosition());
            if (card.isOccupied()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Payment");
                alert.setHeaderText("This card belongs to someone. You have to pay rent");
                alert.setContentText("Rent price: " + card.getRentPrice() + "$");

                alert.showAndWait();
                if (this.users.getCurUser().getBalance() < card.getRentPrice()) {
                    this.users.eliminateUser();
                    if (1 == users.amountOfActivePlayers()) {
                        Alert winAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        winAlert.setTitle("Winning");
                        winAlert.setHeaderText("You won!");
                        winAlert.showAndWait();
                        System.exit(0);
                    }
                } else {
                    this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - card.getRentPrice());
                }
            } else {
                // При недостатньому балансі для покупки продати найдешевшу карточку
                if (this.users.getCurUser().getBalance() >= card.getPrice()) {
                    //Можливо при можливості купування додати 30 сек до таймера
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Would you like to buy property?");
                    alert.setContentText("Price: " + card.getPrice() + "$      " + "Rent price: " + card.getRentPrice() + "$");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        this.users.getCurUser().setBalance(users.getCurUser().getBalance() - card.getPrice());
                        gameField.buyCard(this.users.getCurUser(), card);
                        this.users.getCurUser().boughtCards.add(card);
                    }
                }
            }
        } else if (typeOfCard == CardTypes.CARD_TYPE_CHANCE) {
            generateChanceCard();
        } else if (typeOfCard == CardTypes.CARD_TYPE_SPECIAL_PROPERTY) {
            Card card = this.gameField.cards.get(this.users.getCurUser().getPosition());
            if (card.isOccupied()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment");
                alert.setHeaderText("This card belongs to someone. You have to pay rent");
                alert.setContentText("Rent price: " + card.getRentPrice() + "$");

                alert.showAndWait();
                if (this.users.getCurUser().getBalance() < card.getRentPrice()) {
                    this.users.eliminateUser();
                    if (1 == users.amountOfActivePlayers()) {
                        Alert winAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        winAlert.setTitle("Winning");
                        winAlert.setHeaderText("You won!");
                        winAlert.showAndWait();
                        System.exit(0);
                    }
                } else {
                    this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - card.getRentPrice());
                }
            } else {
                if (this.users.getCurUser().getBalance() >= card.getPrice()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Would you like to buy property?");
                    alert.setContentText("Price: " + card.getPrice() + "$      " + "Rent price: " + card.getRentPrice() + "$");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        this.users.getCurUser().setBalance(users.getCurUser().getBalance() - card.getPrice());
                        gameField.buyCard(this.users.getCurUser(), card);
                        this.users.getCurUser().boughtCards.add(card);
                        ArrayList<Card> cards = this.users.getCurUser().boughtCards;
                        long countGroup_9 = cards.stream()
                                .filter(num -> num.group == 9)
                                .count();
                        long countGroup_10 = cards.stream()
                                .filter(num -> num.group == 10)
                                .count();
                        long countGroup_12 = cards.stream()
                                .filter(num -> num.group == 12)
                                .count();
                        if (countGroup_9 == 2) {
                            for (int i = 0; i < cards.size(); i++) {
                                int temp = cards.get(i).index;
                                if (temp == 3) {
                                    cards.get(i).rentPrice = 620;
                                    gameField.drawPriceLabelText(cards.get(i));
                                } else if (temp == 18) {
                                    cards.get(i).rentPrice = 450;
                                    gameField.drawPriceLabelText(cards.get(i));
                                } else if (temp == 23) {
                                    cards.get(i).rentPrice = 620;
                                    gameField.drawPriceLabelText(cards.get(i));
                                }
                            }
                        }
                        if (countGroup_9 == 3) {
                            for (int i = 0; i < cards.size(); i++) {
                                int temp = cards.get(i).index;
                                if (temp == 3) {
                                    cards.get(i).rentPrice = 920;
                                    gameField.drawPriceLabelText(cards.get(i));
                                } else if (temp == 18) {
                                    cards.get(i).rentPrice = 760;
                                    gameField.drawPriceLabelText(cards.get(i));
                                } else if (temp == 23) {
                                    cards.get(i).rentPrice = 920;
                                    gameField.drawPriceLabelText(cards.get(i));
                                }
                            }
                        }
                        if (countGroup_10 == 2) {
                            for (int i = 0; i < cards.size(); i++) {
                                int temp = cards.get(i).index;
                                if (temp == 8) {
                                    cards.get(i).rentPrice = 100;
                                    gameField.drawPriceLabelText(cards.get(i));
                                } else if (temp == 13) {
                                    cards.get(i).rentPrice = 80;
                                    gameField.drawPriceLabelText(cards.get(i));
                                } else if (temp == 38) {
                                    cards.get(i).rentPrice = 150;
                                    gameField.drawPriceLabelText(cards.get(i));
                                }
                            }
                        }
                        if (countGroup_10 == 3) {
                            for (int i = 0; i < cards.size(); i++) {
                                int temp = cards.get(i).index;
                                if (temp == 8) {
                                    cards.get(i).rentPrice = 500;
                                    gameField.drawPriceLabelText(cards.get(i));
                                } else if (temp == 13) {
                                    cards.get(i).rentPrice = 550;
                                    gameField.drawPriceLabelText(cards.get(i));
                                } else if (temp == 38) {
                                    cards.get(i).rentPrice = 320;
                                    gameField.drawPriceLabelText(cards.get(i));
                                }
                            }
                        }
                        if (countGroup_12 == 2) {
                            for (int i = 0; i < cards.size(); i++) {
                                int temp = cards.get(i).index;
                                if (temp == 28) {
                                    cards.get(i).rentPrice = 450;
                                    gameField.drawPriceLabelText(cards.get(i));
                                } else if (temp == 33) {
                                    cards.get(i).rentPrice = 780;
                                    gameField.drawPriceLabelText(cards.get(i));
                                }
                            }
                        }
                    }
                }
            }
        }

        this.gameField.drawUsersBalances(users);
        if (-1 == users.changeTurn()) {
            Alert winAlert = new Alert(Alert.AlertType.CONFIRMATION);
            winAlert.setTitle("Winning");
            winAlert.setHeaderText("You won!");
            winAlert.showAndWait();
            System.exit(0);
        }
        curTime = Calendar.getInstance().getTime().getTime();
        this.gameField.drawCurPlayerLabelCircle(this.users.getCurUser().getColor());
    }
    //Створення випадкових карточок для поля Chance
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
    //Генерація випадкової події Chance
    public void generateChanceCard() {
        Random random = new Random();
        int chance = random.nextInt(1, 16);
        if (chance == 1) {
            int numberOfCards = this.users.getCurUser().boughtCards.size();
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + numberOfCards * 10);
        } else if (chance == 2) {
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() + 150);
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
            this.users.changeUserPosition(40 + (40 - 20 - this.users.getCurUser().getPosition()));
            this.users.getCurUser().setBalance(this.users.getCurUser().getBalance() - 200);
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

        alert.showAndWait();
        gameField.drawUsers(users);
    }


}
