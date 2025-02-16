package com.main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class User {
    //Поточна позиція у грі (на якій карточці стоїть)
    private int pos = 0;
    //Кількість пройдених кругів
    private int cycle = 0;
    //Баланс
    private float balance = 2000;
    //Колір фішки
    private Color color;
    //Чи програв
    private boolean isEliminated = false;
    //Масив куплених карточок
    public ArrayList<Card> boughtCards = new ArrayList<>();
    //Встановлення кольору фішки
    User(Color color) {
        this.color=color;
    }
    //Отримати колір фішки
    public Color getColor() {
        return color;
    }
    //Змінити колір фішки
    public void setColor(Color color) {
        this.color = color;
    }
    //Змінити позицію на ігровому полі
    public void setPosition(int pos) {
        this.pos += pos;
        if (this.pos > 39) {
            this.cycle++;
            this.pos %= 40;
        }
    }
    //Отримати поточну позицію
    public int getPosition() {
        return pos;
    }
    //Отримати кількість пройдених кругів
    public int getCycle() {
        return cycle;
    }
    //Змінити кількість пройдених кругів
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }
    //Отримати баланс
    public float getBalance() {
        return balance;
    }
    //Змінити баланс
    public void setBalance(float balance) {
        this.balance=balance;
    }
    //Виключити гравця з гри
    public void eliminate() {
        this.isEliminated = true;
    }
    //Перевірити чи гравець програв
    public boolean isEliminated() {
        return this.isEliminated;
    }
}
