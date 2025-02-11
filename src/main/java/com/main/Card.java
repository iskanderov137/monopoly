package com.main;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Card {
    public static int PROPERTY_WIDTH=110;
    public static int PROPERTY_HEIGHT=80;
    public static int CHANCE_HEIGHT=75;

    public int group=0;

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
    public int rentPrice=0;
    public Color borderColor=Color.GREENYELLOW;
    public Pane pn;

    Card(int posX, int posY, int width, int height, CardTypes type, int price, int returnPrice,
         boolean isOccupied,int index, int group,int rentPrice) {
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
        this.rentPrice=rentPrice;
        this.pn = new Pane();
        pn.setTranslateX(posX);
        pn.setTranslateY(posY);
        pn.setPrefSize(width, height);
        BorderStroke borderStroke = new BorderStroke(
                borderColor,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(3)
        );

        pn.setBorder(new Border(borderStroke));
        pn.getChildren().add(new Label(String.valueOf(this.group)));
    }

    public void buyCard(User user){
        this.setBelongs(user);
        BorderStroke borderStroke = new BorderStroke(
                user.getColor(),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(3)
        );
        this.pn.setBorder(new Border(borderStroke));
        this.setOccupied(true);
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