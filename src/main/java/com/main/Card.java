package com.main;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Card {
    public static int PROPERTY_WIDTH=110;
    public static int PROPERTY_HEIGHT=80;
    public static int CHANCE_HEIGHT=75;

    private int posX;
    private int posY;
    private int width;
    private int height;
    private CardTypes type;
    private int price;
    private int returnPrice;
    private boolean isOccupied;
    public int index;
    public Pane pn;

    Card(int posX, int posY, int width, int height, CardTypes type, int price, int returnPrice,
         boolean isOccupied,int index) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.type = type;
        this.price = price;
        this.returnPrice = returnPrice;
        this.isOccupied = isOccupied;
        this.index = index;
        this.pn = new Pane();
        pn.setTranslateX(posX);
        pn.setTranslateY(posY);
        pn.setPrefSize(width, height);

        BorderStroke borderStroke = new BorderStroke(
                Color.GREENYELLOW, // Border color
                BorderStrokeStyle.SOLID, // Border style
                CornerRadii.EMPTY, // Corner radii
                new BorderWidths(3) // Border widths
        );

        pn.setBorder(new Border(borderStroke));
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

//    public int[] getUserPosition(int x, int y){
//        if()
//    }
}