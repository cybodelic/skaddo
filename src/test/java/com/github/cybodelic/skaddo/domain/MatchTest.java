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

    private final int NUMBER_OF_ROUNDS = 120;
    private Match match;
    private List<Player> players;
    private Map<Player, Integer> playerScores = new HashMap<>();

    @Before
    public void setup() {
        // initializing list of players
        players = new ArrayList<>();
        players.add(new Player("Player.1"));
        players.add(new Player("Player.2"));
        players.add(new Player("Player.3"));
        match = new Match();
    }

    /**
     * This method populates the Match instance with NUMBER_OF_ROUNDS rounds randomly created.
     */
    private void generateTestData() {
        match = new Match();

        // initializing map of scores per player
        players.forEach(p -> playerScores.put(p, 0));

        // adding rounds to this match
        IntStream.range(0, this.NUMBER_OF_ROUNDS).forEach(i ->
                {
                    // choosing declarer and score randomly
                    Player p = players.get(new Random().nextInt(this.players.size()));
                    int score = SkatConstants.POSSIBLE_SCORE_VALUES[new Random().nextInt(
                            SkatConstants.POSSIBLE_SCORE_VALUES.length)];
                    match.saveRound(new Round(p, score));
                    // calculate the score and store it for assertions in test cases
                    this.playerScores.put(p, this.playerScores.get(p) + score);
                }
        );
    }

    @Test
    public void getTotalScoreByPlayerTest() {
        generateTestData();
        playerScores.keySet()
                .forEach(p ->
                        assertThat(match.getTotalScoreForPlayer(p)).isEqualTo(playerScores.get(p))
                );
    }

    @Test
    public void getTotalScoreByPlayerV2Test() {
        generateTestData();
        playerScores.keySet()
                .forEach(p ->
                        assertThat(match.getTotalScoreForPlayer(p)).isEqualTo(playerScores.get(p))
                );
    }


    @Test
    public void roundsAddedInRightOrderTest() {
        Arrays.stream(SkatConstants.POSSIBLE_SCORE_VALUES)
            .forEach(e -> match.saveRound(
                new Round(players.get(
                        new Random().nextInt(players.size())), e)
                )
            );

        IntStream.range(0, SkatConstants.POSSIBLE_SCORE_VALUES.length)
            .forEach(i ->
                assertThat(match.getRounds()
                    .get(i)
                    .getScore()).isEqualTo(SkatConstants.POSSIBLE_SCORE_VALUES[i])
            );
    }

    @Test
    public void saveFirstRound() {
        Round m = new Round(players.get(0), SkatConstants.POSSIBLE_SCORE_VALUES[0]);
        match.saveRound(m);
        Assertions.assertThat(match.getRounds())
                .containsOnly(m);
    }

    @Test
    public void saveFirstRoundWithInvalidIndex() {
        Round m = new Round(players.get(0), SkatConstants.POSSIBLE_SCORE_VALUES[0]);
        m.setIndex(5);
        assertThatThrownBy(
                () -> match.saveRound(m)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void saveRounds() {
        IntStream.range(0, SkatConstants.POSSIBLE_SCORE_VALUES.length).forEach(i ->
                match.saveRound(new Round(players.get(
                        ThreadLocalRandom.current()
                                .nextInt(players.size()))
                        , SkatConstants.POSSIBLE_SCORE_VALUES[i]))
        );
        Assertions.assertThat(match.getRounds()
                .size())
                .isEqualTo(SkatConstants.POSSIBLE_SCORE_VALUES.length);
        IntStream.range(0, match.getRounds()
                .size())
                .forEach(i ->
                    Assertions.assertThat(match.getRounds()
                        .get(i)
                        .getIndex())
                        .isEqualTo(i)
                );
    }

    @Test
    public void updateRounds() {
        // save rounds
        IntStream.range(0, SkatConstants.POSSIBLE_SCORE_VALUES.length)
                .forEach(i ->
                    match.saveRound(new Round(players.get((i % players.size()))
                            , SkatConstants.POSSIBLE_SCORE_VALUES[i]))
                );
        Assertions.assertThat(match.getRounds()
                .size())
                .isEqualTo(SkatConstants.POSSIBLE_SCORE_VALUES.length);
        // update rounds by reversing the scores and changing the declarer
        IntStream.range(0, SkatConstants.POSSIBLE_SCORE_VALUES.length)
                .forEach(i -> {
                        Round r = match.getRounds()
                                .get(i);
                        r.setScore(SkatConstants.POSSIBLE_SCORE_VALUES.length - (i + 1));
                        r.setDeclarer(players.get(((i + 1) % players.size())));
                    }
                );
        // check that updates are applied correctly
        IntStream.range(0, SkatConstants.POSSIBLE_SCORE_VALUES.length)
                .forEach(i -> {
                        Round r = match.getRounds().get(i);
                        Assertions.assertThat(r.getIndex())
                                .isEqualTo(i);
                        Assertions.assertThat(r.getScore())
                                .isEqualTo(
                                        SkatConstants.POSSIBLE_SCORE_VALUES.length - (i + 1));
                        Assertions.assertThat(r.getDeclarer())
                                .isEqualTo(
                                        players.get(((i + 1) % players.size()))
                                );
                    }
                );

    }

    @Test
    public void deleteRound() {
        generateTestData();
        List<Round> rounds = match.getRounds();

        // check that its not possible to manipulate list of rounds directly.
        assertThatThrownBy(
                () -> rounds.remove(0))
                .isInstanceOf(UnsupportedOperationException.class
                );

        // save data needed for later assertions
        Round roundToBeDeleted = rounds.get(0);
        int sizeBefore = rounds.size();
        Player declarer = roundToBeDeleted.getDeclarer();
        int scoreBefore = match.getTotalScoreForPlayer(declarer);

        match.deleteRound(roundToBeDeleted);

        assertThat(match.getRounds()
                .size()).isEqualTo(sizeBefore - 1);
        assertThat(match.getTotalScoreForPlayer(declarer))
                .isEqualTo(scoreBefore - roundToBeDeleted.getScore());
        assertThat(match.getRounds()).doesNotContain(roundToBeDeleted);
    }

    @Test
    public void getScoreForPlayerWithoutRoundReturnsZero() {
        assertThat(match.getTotalScoreForPlayer(players.get(0))).isEqualTo(0);
    }

    @Test
    public void getScoreForPlayerNotInPlayerList() {
        assertThat(match.getTotalScoreForPlayer(new Player("UnknownPlayer"))).isEqualTo(0);
    }


    @Test
    public void compareToTest() {
        // TODO implement test
    }
}
