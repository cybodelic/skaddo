package com.github.cybodelic.skaddo.domain;

public class Round implements Comparable<Round> {

    private long id;

    final private Player declarer;

    final private int score;

    private int index;

    public Round(Player declarer, int score) {
        this.declarer = declarer;
        this.score = score;
    }

    public Player getDeclarer() {
        return declarer;
    }

    public int getScore() {
        return score;
    }

    protected int getIndex() {
        return index;
    }

    protected void setIndex(int index){ this.index = index; }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int compareTo(Round r) {
        return (Integer.valueOf(this.index).compareTo(r.getIndex()));
    }
}
