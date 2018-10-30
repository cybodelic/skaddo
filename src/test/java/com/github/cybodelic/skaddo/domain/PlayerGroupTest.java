package com.github.cybodelic.skaddo.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PlayerGroupTest {

    private final String PLAYER_GROUP_NAME = "Demo Group";
    private final int NUMBER_OF_MATCHES = 1000;
    private final int NUMBER_OF_ROUNDS_PER_MATCH = 150;
    Map<Player, Integer> playerScores = new HashMap<>();
    private List<Player> players;
    private PlayerGroup playerGroup;

    @Before
    public void setup() {
        playerGroup = new PlayerGroup(PLAYER_GROUP_NAME);
        players = new ArrayList<>();
        Player p1 = new Player("player1@domainx.net");
        p1.setFirstName("Firstname 1");
        p1.setLastName("Lastname-1");
        p1.setNickName("nick1");
        Player p2 = new Player("player2@domainx.net");
        p2.setFirstName("Firstname2");
        p2.setLastName("Lastname 2");
        p2.setNickName("nick #2");
        Player p3 = new Player("3rdplayer@domainy.info");
        p3.setNickName("Nick3");
        players.add(p1);
        players.add(p2);
        players.add(p3);
        playerGroup.setPlayers(players);
    }

    /**
     * Generate test data (potentially huges volumes) which is not used by every test in this class
     * and therefore is not generated in @Setup method.
     */
    private void generateTestData() {
        // initialize the playerScore maps which is later used for assertions
        playerScores = new HashMap<>();
        players.forEach(player -> playerScores.put(player, 0));

        IntStream.range(0, NUMBER_OF_MATCHES).forEach(iM -> {
                    Match match = new Match();
                    IntStream.range(0, NUMBER_OF_ROUNDS_PER_MATCH).forEach(
                            iR -> {
                                Player player = players.get(new Random().nextInt(players.size()));
                                int score =
                                        SkatConstants.POSSIBLE_SCORE_VALUES[new Random()
                                                .nextInt(SkatConstants.POSSIBLE_SCORE_VALUES.length)];
                                Round round = new Round(player, score);
                                match.saveRound(round);
                                int newScore = playerScores.get(player) + score;
                                playerScores.put(player, newScore);
                            }
                    );
                    playerGroup.saveMatch(match);
                }
        );
    }

    @Test
    public void isNameAndCreatedDateInitialized() {
        assertThat(this.playerGroup.getName()).isEqualTo(PLAYER_GROUP_NAME);
        assertThat(this.playerGroup.getCreatedAt().toLocalDate())
                .isEqualTo(LocalDate.now());
    }

    @Test
    public void getTotalScoreForPlayer() {
        generateTestData();

        // compare player scores returned by method under test with scores saved by this test class.
        players.forEach(player -> assertThat(playerGroup.getTotalScoreForPlayer(player))
                .isEqualTo(playerScores.get(player)));

        // test correct score after deleting a round from one match
        Match matchToBeChanged = playerGroup.getMatches().get(0);
        Round roundToBeRemoved = matchToBeChanged.getRounds().get(0);
        Player declarer = roundToBeRemoved.getDeclarer();
        int previousScore = playerGroup.getTotalScoreForPlayer(declarer);
        int score = roundToBeRemoved.getScore();
        matchToBeChanged.deleteRound(roundToBeRemoved);
        playerGroup.saveMatch(matchToBeChanged);
        int newScore = previousScore - score;
        int newScoreInPlayerGroup = playerGroup.getTotalScoreForPlayer(declarer);
        assertThat(newScoreInPlayerGroup).isEqualTo(newScore);

        // check that only the score of the declarer of the deleted round has changed
        playerScores
                .keySet()
                .stream()
                .filter(p -> !p.equals(declarer))
                .forEach(p -> assertThat(
                        playerGroup.getTotalScoreForPlayer(p))
                        .isEqualTo(playerScores.get(p)));
    }

    @Test
    public void playersCannotBeChangedAfterMatchesHaveBeenSaved() {
        playerGroup.saveMatch(new Match());
        assertThatThrownBy(
                () -> playerGroup.setPlayers(Collections.emptyList())
        ).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void getPlayersReturnsImmutableList() {
        assertThatThrownBy(() ->
                playerGroup.getPlayers().remove(players.get(0)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void saveFirstMatch() {
        Match m = new Match();
        playerGroup.saveMatch(m);
        assertThat(playerGroup.getMatches()).containsOnly(m);
    }

    @Test
    public void saveFirstMatchWithInvalidIndex() {
        Match m = new Match();
        m.setIndex(5);
        assertThatThrownBy(
                () -> playerGroup.saveMatch(m)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void saveMatches() {
        IntStream.range(0, 10).forEach(
                i -> playerGroup.saveMatch(new Match())
        );
        assertThat(playerGroup.getMatches().size()).isEqualTo(10);
        IntStream.range(0, playerGroup.getMatches().size()).forEach(
                i ->
                        assertThat(playerGroup.getMatches().get(i).getIndex()).isEqualTo(i)
        );
    }

    @Test
    public void updateMatches() {
        IntStream.range(0, 10).forEach(i -> playerGroup.saveMatch(new Match()));
        assertThat(playerGroup.getMatches().size()).isEqualTo(10);
        IntStream.range(0, 10).forEach(
                i ->
                        playerGroup.getMatches().get(i).setCreatedAt(
                                LocalDateTime.now().minusDays(i)
                        )
        );
        IntStream.range(0, 10).forEach(
                i ->
                {
                    assertThat(
                            playerGroup.getMatches().get(i).getCreatedAt().toLocalDate())
                            .isEqualTo(
                                    LocalDate.now().minusDays(i)
                            );
                    assertThat(playerGroup.getMatches().get(i).getIndex())
                            .isEqualTo(i);
                }
        );

    }

    @Test
    public void deleteMatch() {
        generateTestData();

        final Match matchToDelete = this.playerGroup.getMatches().get(
                ThreadLocalRandom.current().nextInt(this.playerGroup.getMatches().size())
        );

        Map<Player, Integer> newPlayersScores = new HashMap<>();
        this.playerGroup.getPlayers().forEach(p -> newPlayersScores.put(p,
                this.playerGroup.getTotalScoreForPlayer(p) - matchToDelete
                        .getTotalScoreForPlayer(p)));

        this.playerGroup.deleteMatch(matchToDelete);

        this.playerGroup.getPlayers().forEach(
                p -> assertThat(this.playerGroup.getTotalScoreForPlayer(p))
                        .isEqualTo(newPlayersScores.get(p)));

    }

    @Test
    public void saveMatchWithInvalidNumberOfPlayersInGroup() {
        playerGroup = new PlayerGroup(PLAYER_GROUP_NAME);
        playerGroup.setPlayers(players.subList(0, 1));
        assertThatThrownBy(() -> playerGroup.saveMatch(new Match()))
                .isInstanceOf(IllegalStateException.class);
    }
}
