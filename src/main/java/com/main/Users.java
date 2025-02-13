package com.main;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Users {
    public List<User> users;
    public static int numberOfUsers = 3;
    private int curUser = 0;

    public Users() {
        this.users = new ArrayList<>(numberOfUsers);
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

    public void changeUserPosition(int total) {
        User user = this.getCurUser();
        user.setPosition(total);
    }

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
