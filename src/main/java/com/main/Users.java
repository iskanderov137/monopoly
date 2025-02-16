package com.main;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
//Клас для полегшеного маніпулювання гравцями
public class Users {
    //Список усіх користувачів
    public List<User> users;
    //Кількість гравців
    public static int numberOfUsers = 3;
    //Поточний гравець
    private int curUser = 0;
    //Додавання гравців і встановлення кольорів фішок
    public Users() {
        this.users = new ArrayList<>(numberOfUsers);
        this.users.add(new User(Color.RED));
        this.users.add(new User(Color.YELLOW));
        this.users.add(new User(Color.BLUE));
    }
    //Виключити з гри поточного гравця
    public void eliminateUser() {
        this.getCurUser().eliminate();
    }
    //Отримати поточного гравця
    public User getCurUser() {
        return users.get(curUser);
    }
    //Отримати гравця за індексом у масиві
    public User getUserByIndex(int index) {
        return users.get(index);
    }
    //Змінити позицію поточного користувача
    public void changeUserPosition(int total) {
        User user = this.getCurUser();
        user.setPosition(total);
    }
    //Змінити хід, повертає кількість гравців що ще не програли
    public int changeTurn() {
        int index = curUser;
        int aliveCount = 0;

        for (int i = 0; i < users.size(); i++) {
            if (!this.getUserByIndex(i).isEliminated()) {
                aliveCount++;
            }
        }

        if (aliveCount == 1) {
            return -1;
        }
        for (int i = 1; i < users.size(); i++) {
            int nextIndex = (index + i) % users.size();
            if (!this.getUserByIndex(nextIndex).isEliminated()) {
                curUser = nextIndex;
                return nextIndex;
            }
        }
        return index;
    }
    //Кількість активних гравців
    public int amountOfActivePlayers(){
        int count=0;
        for (int i = 0; i < users.size(); i++) {
            if (!this.getUserByIndex(i).isEliminated()) {
                count++;
            }
        }
        return count;
    }
}
