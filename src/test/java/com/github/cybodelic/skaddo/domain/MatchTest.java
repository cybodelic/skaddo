package com.github.cybodelic.skaddo.domain;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MatchTest {

    private int[] scores = new int[]{18, 120, 23, -72, 48, 27, 36, 22, -96, 46, 33};

    private Match match;

    private List<Player> players;

    @Before
    public void setup() {
        players = new ArrayList<>();
        players.add(new Player("Player.1"));
        players.add(new Player("Player.2"));
        players.add(new Player("Player.3"));
        match = new Match();
    }

    @Test
    public void getTotalScoreByPlayerTest() {
        Map<Player, Integer> pScores = new HashMap<>();
        players.forEach(p -> pScores.put(p, 0));
        IntStream.range(0, this.scores.length).forEach(
                i ->
                {
                    Player p = players.get(new Random().nextInt(3));
                    match.saveRound(new Round(p, this.scores[i]));
                    pScores.put(p, pScores.get(p) + this.scores[i]);
                }
        );
        pScores.keySet().forEach(
                p -> assertThat(match.getTotalScoreForPlayer(p)).isEqualTo(pScores.get(p))
        );
    }

    @Test
    public void roundsAddedInRightOrderTest() {
        Arrays.stream(scores).forEach(e -> match.saveRound(
                new Round(players.get(
                        new Random().nextInt(players.size())), e)
                )
        );

        IntStream.range(0, scores.length).forEach(
                i ->
                        assertThat(match.getRounds().get(i).getScore()).isEqualTo(scores[i])
        );
    }

    @Test
    public void saveFirstRound() {
        Round m = new Round(players.get(0), scores[0]);
        match.saveRound(m);
        Assertions.assertThat(match.getRounds()).containsOnly(m);
    }

    @Test
    public void saveFirstRoundWithInvalidIndex() {
        Round m = new Round(players.get(0), scores[0]);
        m.setIndex(5);
        assertThatThrownBy(
                () -> match.saveRound(m)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void saveRounds() {
        IntStream.range(0, scores.length).forEach(
                i ->
                        match.saveRound(new Round(players.get(
                                ThreadLocalRandom.current().nextInt(players.size()))
                                , scores[i]))
        );
        Assertions.assertThat(match.getRounds().size()).isEqualTo(scores.length);
        IntStream.range(0, match.getRounds().size()).forEach(
                i ->
                        Assertions.assertThat(match.getRounds().get(i).getIndex()).isEqualTo(i)
        );
    }

    @Test
    public void updateRounds() {
        // save rounds
        IntStream.range(0, scores.length).forEach(
                i ->
                        match.saveRound(new Round(players.get((i % players.size()))
                                , scores[i]))
        );
        Assertions.assertThat(match.getRounds().size()).isEqualTo(scores.length);
        // update rounds by reversing the scores and changing the declarer
        IntStream.range(0, scores.length).forEach(
                i -> {
                    Round r = match.getRounds().get(i);
                    r.setScore(scores.length - (i + 1));
                    r.setDeclarer(players.get(((i + 1) % players.size())));
                }
        );
        // check that updates are applied correctly
        IntStream.range(0, scores.length).forEach(
                i ->
                {
                    Round r = match.getRounds().get(i);
                    Assertions.assertThat(r.getIndex()).isEqualTo(i);
                    Assertions.assertThat(r.getScore()).isEqualTo(scores.length - (i + 1));
                    Assertions.assertThat(r.getDeclarer()).isEqualTo(
                            players.get(((i + 1) % players.size()))
                    );
                }
        );

    }

    @Test
    public void deleteRound() {
        IntStream.range(0, scores.length).forEach(
                i ->
                        match.saveRound(new Round(players.get(
                                ThreadLocalRandom.current().nextInt(players.size()))
                                , scores[i]))
        );
        List<Round> rounds = match.getRounds();
        assertThatThrownBy(
                () -> rounds.remove(0))
                .isInstanceOf(UnsupportedOperationException.class
                );
        // TODO implement boundary test (IntStream.of(....))
        Round roundToBeDeleted = rounds.get(0);
        int sizeBefore = rounds.size();
        Player declarer = roundToBeDeleted.getDeclarer();
        int scoreBefore = match.getTotalScoreForPlayer(declarer);
        match.deleteRound(roundToBeDeleted);
        assertThat(match.getRounds().size()).isEqualTo( sizeBefore - 1);
        assertThat(match.getTotalScoreForPlayer(declarer)).isEqualTo(scoreBefore - roundToBeDeleted.getScore());
        assertThat(match.getRounds()).doesNotContain(roundToBeDeleted);
    }

    @Test
    public void compareToTest() {
        // TODO implement test
    }
}
