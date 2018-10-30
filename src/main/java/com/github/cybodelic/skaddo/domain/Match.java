package com.github.cybodelic.skaddo.domain;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Match implements Comparable<Match> {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private int index;

    private LocalDateTime createdAt;

    @OneToMany
    private List<Round> rounds;

    public Match() {
        this.createdAt = LocalDateTime.now();
        this.rounds = new ArrayList<>();
        this.index = -1;
    }

    protected int getIndex() {
        return index;
    }

    protected void setIndex(int index) {
        this.index = index;
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

    public void saveRound(Round round) {
        // TODO add an algorithm which checks that the given score value is valid Skat score value.
        if (round.getScore() == 0) throw new IllegalStateException(
                "Round cannot be added because a score of 0 is not valid.");

        int index = round.getIndex();

        if (index > this.rounds.size() || index < -1) throw new IndexOutOfBoundsException(
                String.format("Invalid round index %d." , index));

        if (round.getIndex() == -1) {
            round.setIndex(this.rounds.size());
            this.rounds.add(round);
        } else {
            this.rounds.set(round.getIndex(), round);
        }
    }

    public void deleteRound(Round round) {
        if (!this.getRounds().contains(round))
            throw new IndexOutOfBoundsException(
                    String.format(
                            "Round with index=%d cannot be removed because it is not in" +
                                    " list of rounds for this match."
                            , round.getIndex()));

        this.rounds.removeIf(r -> r.getIndex() == round.getIndex());
    }

    public List<Round> getRounds() {
        return Collections.unmodifiableList(rounds);
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
        return Integer.compare(this.getIndex(), other.getIndex());
    }
}