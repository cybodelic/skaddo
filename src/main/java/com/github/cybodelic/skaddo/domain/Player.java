package com.github.cybodelic.skaddo.domain;

import org.springframework.data.annotation.Id;

/**
 * Created by axel on 13.07.17.
 */
public class Player {

    @Id
    private String userID;

    private String firstName;

    private String lastName;

    private String nickName;

    public Player() {
        userID = "default";
    }

    public Player(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
