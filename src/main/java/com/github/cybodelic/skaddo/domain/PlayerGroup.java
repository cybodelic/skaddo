package com.github.cybodelic.skaddo.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity
public class PlayerGroup {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "PlayerGroup.name may not be null")
    private String name;

    @ManyToMany
    private List<Player> players;

    @OneToMany
    private List<Match> matches;

    @ElementCollection
    private Map<String, Integer> scores;

    @OneToOne
    private Player createdBy;

    private LocalDateTime createdAt;

    public PlayerGroup() {
        players = new ArrayList<>();
        matches = new ArrayList<>();
        createdAt = LocalDateTime.now();
    }

    public PlayerGroup(String name) {
        this();
        this.setName(name);
    }

    public int getTotalScoreForPlayer(Player player) {
        return this.matches.stream().mapToInt(m -> m.getTotalScoreForPlayer(player)).sum();
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void setPlayers(List<Player> players) {
        if (this.matches.size() > 0)
            throw new UnsupportedOperationException(
                    "Players of playergroup cannot be changed after matches have been saved.");
        this.players = players;
    }

    public Long getId() {
        return id;
    }

    //TODO check if this method can be removed (should only be used by persistence framework)
    public void setId(Long id) {
        this.id = id;
    }

    public List<Match> getMatches() {
        return this.matches;
    }

    public Player getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Player createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }
}
