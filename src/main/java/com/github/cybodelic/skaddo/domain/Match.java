package com.github.cybodelic.skaddo.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Match implements Comparable <Match> {

    private long id;
    private LocalDate date;
    private List<Round> rounds;

    public Match() {
        date = LocalDate.now();
        rounds = new ArrayList<Round>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTotalScoreForPlayer(Player p) {
        return this.rounds.stream()
                .filter(r -> r.getDeclarer() == p)
                .mapToInt(Round::getScore)
                .sum();
    }

    public void addRound(Round round) {
        round.setIndex(rounds.size());
        rounds.add(round.getIndex(), round);
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    @Override
    public int compareTo(Match other) {
        return this.getDate().compareTo(other.getDate());
    }
}