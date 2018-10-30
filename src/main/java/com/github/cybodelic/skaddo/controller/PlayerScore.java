package com.github.cybodelic.skaddo.controller;

public class PlayerScore {

    private String player;

    private long score;

    public PlayerScore(String player, long score) {
        super();
        this.player = player;
        this.score = score;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
