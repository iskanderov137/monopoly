package com.main;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

public class Card {
    public static int PROPERTY_WIDTH = 110;
    public static int PROPERTY_HEIGHT = 80;
    public static int CHANCE_HEIGHT = 75;

    public int group;

    private User belongs;
    private int posX;
    private int posY;
    private int width;
    private int height;
    private CardTypes type;
    private int price;
    private int returnPrice;
    private boolean isOccupied;
    public int index;
    public int rentPrice;
    public int rentPriceWithHouses;
    public int upgradePrice;
    public int level = 0;
    public Color borderColor = Color.TRANSPARENT;

    Card(int posX, int posY, int width, int height, CardTypes type, int price, int returnPrice,
         boolean isOccupied, int index, int group, int rentPrice, int upgradePrice) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.type = type;
        this.price = price;
        this.returnPrice = returnPrice;
        this.isOccupied = isOccupied;
        this.index = index;
        this.group = group;
        this.rentPrice = rentPrice;
        this.upgradePrice = upgradePrice;
        this.rentPriceWithHouses = rentPrice;

    }

    public CardTypes getType() {
        return type;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setType(CardTypes type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public int getRentPrice() {
        if (this.level == 0 || type == CardTypes.CARD_TYPE_SPECIAL_PROPERTY) {
            return rentPrice;
        } else {
            return rentPriceWithHouses;
        }
    }

    public void setRentPrice(int rentPrice) {
        this.rentPrice = rentPrice;
    }

    public void upgradeSpecialCardRentPrice() {
        if (group == 9) {
            if (level == 0) {
                if (this.rentPrice == 280) {
                    this.rentPrice = 620;
                }
                if (this.rentPrice == 220) {
                    this.rentPrice = 450;
                }
                this.level++;
            } else {
                if (this.rentPrice == 620) {
                    this.rentPrice = 920;
                }
                if (this.rentPrice == 450) {
                    this.rentPrice = 760;
                }
            }
        }
    }

    public void upgradeCardRentPrice() {
        if (level == 0) {
            level = 5;
        }
        if (level == 5) {
            level = 15;
        } else if (level == 15) {
            level = 30;
        } else if (level == 30) {
            level = 45;
            this.upgradePrice += 10;
        } else if (level == 45) {
            level = 50;
        }
        this.rentPriceWithHouses = level * rentPrice;
    }

    public int getReturnPrice() {
        return returnPrice;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setReturnPrice(int returnPrice) {
        this.returnPrice = returnPrice;
    }

    public User getBelongs() {
        return belongs;
    }

    public void setBelongs(User belongs) {
        this.belongs = belongs;
    }
}