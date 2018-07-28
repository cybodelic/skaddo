package com.github.cybodelic.skaddo.domain;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

public class Match implements Comparable<Match> {

    private int index;
    private LocalDate date;
    private List<Round> rounds;
    private Map<Player, Integer> playerScore;

    public Match() {
        this.date = LocalDate.now();
        this.rounds = new ArrayList<>();
        this.playerScore = new HashMap<>();
        this.index = -1;
    }

    protected int getIndex() {
        return index;
    }

    protected void setIndex(int index) {
        this.index = index;
    }

    public int getTotalScoreForPlayer(Player player) {
        if (this.playerScore.containsKey(player))
            return this.playerScore.get(player);
        return 0;
    }

    public void saveRound(Round round) {
        if (round.getScore() == 0) throw new IllegalStateException(
                "Round cannot be added because a score of 0 is not valid."
        );
        /**if (this.rounds.size() != 0 && round.getIndex() == 0) {
            round.setIndex(this.rounds.size());
            this.rounds.add(round);
        }
        else if (this.rounds.size() !=0 && round.getIndex() != 0)
            this.rounds.set(round.getIndex(), round);
        else if (this.rounds.size() == 0 && round.getIndex() == 0)
            this.rounds.add(round);
        else
            throw new IllegalStateException(
                    String.format("Round cannot be added. Please check if index %d is correct.",
                            round.getIndex())
            );
         **/
        if (round.getIndex() == -1) {
            round.setIndex(this.rounds.size());
            this.rounds.add(round);
        } else {
            this.rounds.set(round.getIndex(), round);
        }

        // update the players score
        Player declarer = round.getDeclarer();
        if ( !this.playerScore.containsKey(declarer))
            this.playerScore.put(declarer, 0);

        int newScore = Math.addExact(this.playerScore.get(declarer), round.getScore());
        this.playerScore.put(round.getDeclarer(), newScore);
    }

    public void deleteRound(Round round) {
        if (!this.getRounds().contains(round))
            throw new IndexOutOfBoundsException(
                    String.format("Round with index=%d cannot be removed because it is not in list of rounds for this match."
                            , round.getIndex()));

        // update the players score
        Player declarer = round.getDeclarer();
        int newScore = Math.subtractExact(this.playerScore.get(declarer), round.getScore());
        this.playerScore.put(round.getDeclarer(), newScore);
        this.rounds.removeIf(r -> r.getIndex() == round.getIndex());
    }

    public List<Round> getRounds() {
        return Collections.unmodifiableList(rounds);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    @Override
    public int compareTo(Match other) {
        return Integer.compare(this.getIndex(), other.getIndex());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                //requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }
}