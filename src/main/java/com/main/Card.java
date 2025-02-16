package com.main;

import javafx.scene.paint.Color;
//Клас для відображення карточок
public class Card {
    //Ширина звичайної карточки
    public static int PROPERTY_WIDTH = 110;
    //Висота звичайної карточки
    public static int PROPERTY_HEIGHT = 80;
    //Висота карточки з шансом
    public static int CHANCE_HEIGHT = 75;
    //Номер групи карточки
    public int group;
    //Якому гравцю належить карточка
    private User belongs;
    //Позицію карточки по Х
    private int posX;
    //Позицію карточки по У
    private int posY;
    //Ширина карточки
    private int width;
    //Висота карточки
    private int height;
    //Тип карточки
    private CardTypes type;
    //Ціна карточки
    private int price;
    //Ціна карточки при поверненні
    private int returnPrice;
    //Чи карточка куплена
    private boolean isOccupied;
    //Індекс карточки у загальному масиві карточок
    public int index;
    //Ціна ренти
    public int rentPrice;
    //Ціна ренти якщо є карточки з однаковою групою
    public int rentPriceWithHouses;
    //Ціна покращення
    public int upgradePrice;
    //Поточний рівень карточки
    public int level = 0;
    //Колір карточки
    public Color borderColor = Color.TRANSPARENT;
    //Встановлення усіх вище зазначених властивостей для карточки
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
    //Отримати тип карточки
    public CardTypes getType() {
        return type;
    }
    //Отримати позицію по Х
    public int getPosX() {
        return posX;
    }
    //Отримати позицію по У
    public int getPosY() {
        return posY;
    }
    //Отримати ширину карточки
    public int getWidth() {
        return width;
    }
    //Отримати висоту карточки
    public int getHeight() {
        return height;
    }
    //Отримати ціну покупки
    public int getPrice() {
        return price;
    }
    //Отримати ціну ренти
    public int getRentPrice() {
        if (this.level == 0 || type == CardTypes.CARD_TYPE_SPECIAL_PROPERTY) {
            return rentPrice;
        } else {
            return rentPriceWithHouses;
        }
    }
    //Чи карточка належить комусь
    public boolean isOccupied() {
        return isOccupied;
    }
    //Привласнити карточку
    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
    //Привласнити карточку якомусь гравцю
    public void setBelongs(User belongs) {
        this.belongs = belongs;
    }
}