package com.main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class User {
    private int pos = 0;
    private int posX;
    private int posY;
    private int cycle = 0;
    private Color color;
    public Circle circle;

    User(Color color, int posX, int posY) {
        this.circle = new Circle();
        circle.setCenterX(posX);
        circle.setCenterY(posY);
        circle.setRadius(20);
        circle.setFill(color);
        circle.setStroke(color);
        circle.setStrokeWidth(1);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int newPosX) {
        this.posX = newPosX;
        circle.setCenterX(this.posX);
    }

    public void setPosY(int newPosY) {
        this.posY = newPosY;
        circle.setCenterY(this.posY);
    }

    public void setPosition(int pos) {
        if (pos > 39) {
            this.cycle++;
            pos %= 40;
        }
        System.out.println("Cycle: " + this.cycle);
        this.pos = pos;
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
}
