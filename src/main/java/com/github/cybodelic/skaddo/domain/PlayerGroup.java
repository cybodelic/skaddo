package com.github.cybodelic.skaddo.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class PlayerGroup {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    private List<Player> players;

    @OneToMany
    private List<Match> matches;

    @NotNull(message = "PlayerGroup.name may not be null")
    private String name;

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
        return Collections.unmodifiableList(matches);
    }

    public void saveMatch(Match match) {
        if (this.players.size() != 3 && this.players.size() != 4)
            throw new IllegalStateException(
                    "PlayerGroup must have three or four players before a match can be saved.");

        if (match.getIndex() == -1) {
            // a new match is being added
            match.setIndex(this.matches.size());
            this.matches.add(match);
        } else {
            // a match is updated
            try {
                this.matches.set(match.getIndex(), match);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalStateException(
                        String.format(
                                "Trying to add a match with an invalid index %d. Current list of " +
                                        "matches has size %d."
                                , match.getIndex()
                                , getMatches().size())
                );
            }
        }
    }

    public void deleteMatch(Match match) {
        if (!this.matches.contains(match)) {
            throw new IllegalArgumentException(
                    String.format(
                            "Match with index=%d cannot be removed because it is not in list of " +
                                    "matches for this playerGroup"
                            , match.getIndex()));
        }
        this.matches.remove(match);
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
