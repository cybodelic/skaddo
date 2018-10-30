package com.github.cybodelic.skaddo.data;

import com.github.cybodelic.skaddo.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerGroupRepositoryTest {

    private final String[] TESTGROUP_NAMES = {"Demo group 1", "Demo group 2", "Demo group 3",
            "Demo group 4"};
    private final int NUMBER_OF_MATCHES = 100;
    private final int NUMBER_OF_ROUNDS_PER_MATCH = 150;

    private List<Player> players;
    // this playergroup is setup by testcase saveFirstPlayerGroupWithMatchesAndRounds and can be
    // used by any other test case which calls this method
    private PlayerGroup testPlayerGroup;
    // also populated by saveFirstPlayerGroupWithMatchesAndRounds, can be used to assert correct
    // score for players
    private Map<Player, Integer> playerScores = new HashMap<>();

    @Autowired
    private PlayerGroupRepository playerGroupRepository;
    @Autowired
    private PlayerRepository playerRepo;


    {
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
    }

    @Before
    public void savePlayerGroupAndPlayers() {
        playerGroupRepository.deleteAll();
        playerRepo.deleteAll();
        players = playerRepo.saveAll(players);
        Player creator = players.get(ThreadLocalRandom.current().nextInt(players.size()));
        Arrays.stream(TESTGROUP_NAMES).forEach(n -> {
            PlayerGroup group = new PlayerGroup(n);
            group.setCreatedBy(creator);
            group.setPlayers(players);
            playerGroupRepository.save(group);
        });

        int sizeAfter = playerGroupRepository.findAll().size();
        assertThat(Integer.valueOf(sizeAfter)).isEqualTo(TESTGROUP_NAMES.length);
    }

    @Test
    public void saveFirstPlayerGroupWithMatchesAndRounds() {

        List<PlayerGroup> groups = playerGroupRepository.findAllByName(TESTGROUP_NAMES[0]);
        final PlayerGroup group = groups.get(0);

        // initializing map of scores per player
        players.forEach(p -> playerScores.put(p, 0));

        IntStream.range(0, NUMBER_OF_MATCHES).forEach(a -> {
            Match match = new Match();
            int day = 14;
            int month = (a % 12) + 1;
            int year = 2000 + (int) Math.floor(a / 12);
            match.setCreatedAt(
                    LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.now()));

            IntStream.range(0, NUMBER_OF_ROUNDS_PER_MATCH).forEach(i -> {
                Player p = players.get(new Random().nextInt(players.size()));
                int score = SkatConstants.POSSIBLE_SCORE_VALUES[new Random().nextInt(
                        SkatConstants.POSSIBLE_SCORE_VALUES.length)];
                match.saveRound(new Round(p, score));
                this.playerScores.put(p, this.playerScores.get(p) + score);
            });
            group.saveMatch(match);
        });

        testPlayerGroup = playerGroupRepository.save(group);
        assertThat(testPlayerGroup.getMatches().size()).isEqualTo(NUMBER_OF_MATCHES);
    }

    @Test
    public void testUpdatePlayerAndCheckThatReferenceIsUpdatedToo() {
        List<PlayerGroup> groups = playerGroupRepository.findAllByName(TESTGROUP_NAMES[0]);
        PlayerGroup group = groups.get(0);
        String gID = group.getId();
        Player creator = group.getCreatedBy();
        String newNickName = "new nickname";
        creator.setNickName(newNickName);
        creator = playerRepo.save(creator);
        group = playerGroupRepository.findById(gID).get();
        assertThat(group.getCreatedBy().getNickName()).isEqualTo(creator.getNickName());
        assertThat(group.getCreatedBy().getUserID()).isEqualTo(creator.getUserID());
        assertThat(group.getPlayers()).filteredOn("nickName", newNickName).size().isEqualTo(1);
    }

    @Test
    public void testUpdateRounds() {

        saveFirstPlayerGroupWithMatchesAndRounds();

        PlayerGroup group = playerGroupRepository.findAllByName(TESTGROUP_NAMES[0]).get(0);
        Match lastMatch = group.getMatches().get(group.getMatches().size() - 1);
        int numberOfRounds = lastMatch.getRounds().size();
        Round roundToBeRemoved = lastMatch.getRounds().get(0);
        Player player = roundToBeRemoved.getDeclarer();
        int player1TotalScore = lastMatch.getTotalScoreForPlayer(player);
        assertThat(numberOfRounds).isGreaterThan(0);

        lastMatch.deleteRound(roundToBeRemoved);
        group = playerGroupRepository.save(group);
        lastMatch = group.getMatches().get(group.getMatches().size() - 1);
        assertThat(lastMatch.getRounds().size()).isEqualTo(numberOfRounds - 1);
        assertThat(lastMatch.getTotalScoreForPlayer(player)).isNotEqualTo(player1TotalScore);
    }

    @Test
    public void getTotalPlayerScore() {
        saveFirstPlayerGroupWithMatchesAndRounds();

        long start = System.currentTimeMillis();
        final PlayerGroup group = playerGroupRepository.findAllByName(TESTGROUP_NAMES[0]).get(0);
        players.forEach(p -> assertThat(group.getTotalScoreForPlayer(p)).isEqualTo(playerScores.get(p)));
        long duration = System.currentTimeMillis() - start;
        System.out.println("Duration: " + duration);
    }

    @Test
    public void testFindAllByName() {
        List<PlayerGroup> results = playerGroupRepository.findAllByName(TESTGROUP_NAMES[0]);
        assertThat(results.size()).isEqualTo(1);
    }

    @Test
    public void testFindAllByPlayersUserID() {
        List<PlayerGroup> p1Groups = playerGroupRepository
                .findAllByPlayersUserID(players.get(0).getUserID());
        assertThat(p1Groups.size()).isEqualTo(TESTGROUP_NAMES.length);
    }
}
