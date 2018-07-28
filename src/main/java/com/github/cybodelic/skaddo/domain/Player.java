package com.github.cybodelic.skaddo.domain;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by axel on 13.07.17.
 */
public class Player {

    @Id
    @NotNull(message = "Player.userID may not be null")
    private String userID;

    private LocalDateTime createdAt;

    private String firstName;

    private String lastName;

    private String composedName;

    @NotNull(message = "Player.nickName may not be null")
    private String nickName;

    public Player() {
        super();
        this.setCreatedAt(LocalDateTime.now());
    }

    public Player(String userID) {
        this();
        this.setUserID(userID);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
        this.composeName();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
        this.composeName();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName.trim();
        this.composeName();
    }

    public String getComposedName(){
        return this.composedName;
    }

    private void composeName(){
        this.composedName = String.format("%s %s %s",
                Objects.toString(this.firstName, ""),
                Objects.toString(this.nickName, ""),
                Objects.toString(this.lastName, "")
        ).trim();
    }
}
