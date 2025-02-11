package com.main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class User {
    private int pos = 0;
    private int cycle = 0;
    private float balance = 2000;
    private Color color;
    private boolean isEliminated = false;
    public ArrayList<Card> boughtCards = new ArrayList<>();

    User(Color color) {
        this.color=color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPosition(int pos) {
        this.pos += pos;
        if (this.pos > 39) {
            this.cycle++;
            this.pos %= 40;
        }
    }

    public int getPosition() {
        return pos;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public void buyCard(Card card) {
        if (this.balance > card.getPrice()) {
            this.boughtCards.add(card);
            this.balance -= card.getPrice();
            card.setBelongs(this);
            card.setOccupied(true);
        }
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance=balance;
    }

    public void eliminate() {
        this.isEliminated = true;
    }

    public boolean isEliminated() {
        return this.isEliminated;
    }
}
