package com.github.cybodelic.skaddo.domain;

import java.util.ArrayList;
import java.util.List;

public class PlayerGroup {

    private long id;

    private List<Player> players;

    private List<Match> matches;

    private String name;


    public PlayerGroup() {
        players = new ArrayList<Player>();
        matches = new ArrayList<Match>();
    }

    public PlayerGroup(String name) {
        this();
        this.name = name;
    }

    public int getTotalScoreForPlayer(Player p) {
        //TODO implement
        return 0;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlayer(Player p) {
        this.players.add(p);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
