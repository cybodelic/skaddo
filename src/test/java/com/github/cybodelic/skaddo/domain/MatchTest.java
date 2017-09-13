package com.github.cybodelic.skaddo.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MatchTest {

    private int[] scores;

    private Match match;

    private List<Player> players;

    @Before
    public void initMatch() {
        players = new ArrayList<Player>();
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        players.add(new Player("Player 3"));
        match = new Match();
        scores = new int[]{18, 120, 23, -72, 48, 27, 36, 22, -96, 46, 33};
    }

    @Test
    public void getTotalScoreByPlayerTest() {

        Map<Player, Integer> pScores = new HashMap<Player, Integer>();
        for (Player p : players) {
            pScores.put(p, 0);
        }


        for (int i = 0; i < this.scores.length; i++) {
            Player p = players.get(new Random().nextInt(3));
            match.addRound(new Round(p, this.scores[i]));
            pScores.put(p, pScores.get(p) + this.scores[i]);
        }

        for (Player p : pScores.keySet()) {
            assertThat(
                    "Players score is calculated correctly",
                    match.getTotalScoreForPlayer(p),
                    is(pScores.get(p)));
        }
    }

    @Test
    public void roundsAddedInRightOrderTest() {
        Arrays.stream(scores).forEach(e -> {
            match.addRound(new Round(players.get(new Random().nextInt(3)), e));
        });

        for (int i = 0; i < scores.length; i++) {
            assertThat("round is at correct index", match.getRounds().get(i).getScore(), is(scores[i]));
        }
    }

    @Test
    public void compareToTest() {
        // TODO implement test
    }
}
