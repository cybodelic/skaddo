package com.github.cybodelic.skaddo.domain;

public class Round implements Comparable<Round> {

    private Player declarer;

    private int score;

    private int index;

    public Round(Player declarer, int score) {
        this.declarer = declarer;
        this.score = score;
        this.index = -1;
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

    protected void setIndex(int index){ this.index = index; }

    @Override
    public int compareTo(Round r) {
        return (Integer.valueOf(this.index).compareTo(r.getIndex()));
    }
}
