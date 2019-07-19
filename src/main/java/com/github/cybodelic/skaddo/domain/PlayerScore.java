package com.github.cybodelic.skaddo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class PlayerScore {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Player player;

    private long score;

    public PlayerScore(Player player, long score) {
        super();
        this.player = player;
        this.score = score;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
