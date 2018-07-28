package com.github.cybodelic.skaddo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

public class PlayerGroup {

    @Id
    private String id;

    @DBRef
    private List<Player> players;

    private List<Match> matches;

    @NotNull(message = "PlayerGroup.name may not be null")
    private String name;

    @DBRef
    @NotNull(message = "PlayerGroup.createdBy may not be null")
    private Player createdBy;

    private LocalDateTime createdAt;

    private Map<Player, Integer> playerScore;

    public PlayerGroup() {
        players = new ArrayList<>();
        matches = new ArrayList<>();
        createdAt = LocalDateTime.now();
        playerScore = new HashMap<>();
    }

    public PlayerGroup(String name) {
        this();
        this.setName(name);
    }

    public int getTotalScoreForPlayer(Player player) {
        if (this.playerScore.containsKey(player))
            return this.playerScore.get(player);
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void setPlayers(List<Player> players) {
        if (this.matches.size() > 0)
            throw new UnsupportedOperationException("Players of playergroup cannot be changed after matches have been saved.");
        this.players = players;
        this.playerScore = new HashMap<>();
        players.forEach(p -> this.playerScore.put(p, 0));
    }

    public String getId() {
        return id;
    }

    //TODO check if this method can be removed (should only be used by persistence framework)
    public void setId(String id) {
        this.id = id;
    }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    public void saveMatch(Match match) {
        if (this.players.size() != 3 && this.players.size() != 4)
            throw new IllegalStateException("PlayerGroup must have three or four players before a match can be saved.");

        if (match.getIndex() == -1) {
            // a new match is being added
            match.setIndex(this.matches.size());
            this.matches.add(match);
            this.playerScore.forEach(
                    (player, currentScore) -> {
                        int newScore = currentScore + match.getTotalScoreForPlayer(player);
                        this.playerScore.put(player, newScore);
                    });
        } else {
            // a match is updated
            try {
                this.matches.set(match.getIndex(), match);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalStateException(
                        String.format(
                                "Trying to add a match with an invalid index %d. Current list of matches has size %d."
                                , match.getIndex()
                                , getMatches().size())
                );
            }

            // player scores have to be recalculated completely by iterating over all matches
            this.playerScore.forEach((k,v) -> this.playerScore.put(k, 0));
            this.getMatches().forEach(
                    m -> this.playerScore.forEach(
                            (player, currentScore) -> {
                                int newScore = currentScore + m.getTotalScoreForPlayer(player);
                                this.playerScore.put(player, newScore);
                            })
            );
        }


    }

    public void deleteMatch(Match match) {
        //TODO implement deleteMatch method
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
}
