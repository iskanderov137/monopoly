package com.main;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static com.main.Card.PROPERTY_HEIGHT;

public class Users {
    public List<User> users;
    public static int numberOfUsers=3;
    private int curUser = 0;

    public Users(){
        this.users=new ArrayList<>(numberOfUsers);
        this.users.add(new User(Color.RED));
        this.users.add(new User(Color.YELLOW));
        this.users.add(new User(Color.BLUE));
    }

    public void eliminateUser() {
        this.getCurUser().eliminate();
    }

    public User getCurUser() {
        return users.get(curUser);
    }

    public User getUserByIndex(int index) {
        return users.get(index);
    }

    public void changeUserPosition(int total){
        User user=this.getCurUser();
        user.setPosition(total);
    }

    public void changeTurn() {
        int index = curUser;
        int nextIndex;
        do {
            nextIndex = (index + 1) % users.size();
            if (!this.getUserByIndex(nextIndex).isEliminated()) {
                curUser = nextIndex;
                break;
            }
        } while (index != nextIndex);

    }
}
