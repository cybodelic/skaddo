package com.github.cybodelic.skaddo.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.lessThan;

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
                    match.getRounds().add(new Round(p, score));
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
    public void roundsAddedInRightOrderTest() {
        Arrays.stream(SkatConstants.POSSIBLE_SCORE_VALUES)
            .forEach(e -> match.getRounds().add(
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
    public void getScoreForPlayerWithoutRoundReturnsZero() {
        assertThat(match.getTotalScoreForPlayer(players.get(0))).isEqualTo(0);
    }

    @Test
    public void getScoreForPlayerNotInPlayerList() {
        assertThat(match.getTotalScoreForPlayer(new Player("UnknownPlayer"))).isEqualTo(0);
    }


    @Test
    public void compareToTest() throws InterruptedException {
        Match m1 = new Match();
        Thread.sleep(2);
        Match m2 = new Match();
        Assert.assertThat(m1.compareTo(m2), lessThan(0));
    }

    @Test
    public void compareToNullTest() throws InterruptedException {
        Match m1 = new Match();
        assertThatThrownBy( () -> m1.compareTo(null)).isInstanceOf(NullPointerException.class);
    }
}
