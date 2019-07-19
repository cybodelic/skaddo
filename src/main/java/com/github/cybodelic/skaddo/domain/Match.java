package com.github.cybodelic.skaddo.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Match implements Comparable<Match> {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    @OrderBy("createdAt DESC")
    private List<Round> rounds;

    @ElementCollection
    private Map<String, Integer> scores;

    private LocalDateTime createdAt;

    public Match() {
        this.createdAt = LocalDateTime.now();
        this.rounds = new ArrayList<>();
        this.scores = new HashMap<>();
    }

    public Long getId() {
        return id;
    }

    /**
     * Get the total sum of scores from all rounds for the player.
     *
     * @param player the player
     * @return The total sum of scores, zero if player has no score yet or player is not part of
     * the players list.
     */
    public int getTotalScoreForPlayer(Player player) {
        return this.getRounds().stream().filter(r -> r.getDeclarer().equals(player))
                .mapToInt(Round::getScore).sum();
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public List<Round> getRounds() {
        return this.rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Rounds are ordered in their chronological appearance.
     *
     * @param other the match to compare to
     * @return standard compareTo return values
     */
    @Override
    public int compareTo(Match other) {
        if ( null == other ) {
            throw new NullPointerException("Cannot compare to null");
        }
        return this.createdAt.compareTo(other.getCreatedAt());
    }
}