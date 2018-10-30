package com.github.cybodelic.skaddo.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class Round implements Comparable<Round> {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @OneToOne
    private Player declarer;
    private LocalDateTime createdAt;
    private int score;
    private int index;

    public Round() {
        super();
        this.createdAt = LocalDateTime.now();
    }

    public Round(Player declarer, int score) {
        this.declarer = declarer;
        this.score = score;
        this.index = -1;
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

    protected int getIndex() {
        return index;
    }

    protected void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(Round r) {
        return (Integer.compare(this.index, r.getIndex()));
    }
}
