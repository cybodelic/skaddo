package com.github.cybodelic.skaddo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class Round implements Comparable<Round> {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Player declarer;

    private LocalDateTime createdAt;

    private int score;

    public Round() {
        super();
        this.createdAt = LocalDateTime.now();
    }

    public Round(Player declarer, int score) {
        this.declarer = declarer;
        this.score = score;
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Player getDeclarer() {
        return declarer;
    }

    public void setDeclarer(Player declarer) {
        this.declarer = declarer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    @Override
    public int compareTo(Round r) {
        if ( null == r ) {
            throw new NullPointerException("Cannot compare to null");
        }
        return this.createdAt.compareTo(r.getCreatedAt());
    }
}
