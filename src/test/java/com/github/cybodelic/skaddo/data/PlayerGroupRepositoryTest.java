package com.github.cybodelic.skaddo.data;

import com.github.cybodelic.skaddo.domain.Match;
import com.github.cybodelic.skaddo.domain.Player;
import com.github.cybodelic.skaddo.domain.PlayerGroup;
import com.github.cybodelic.skaddo.domain.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerGroupRepositoryTest {

    final private String TESTGROUP_1_NAME = "Demo group";
    @Autowired
    private PlayerGroupRepository playerGroupRepository;
    @Autowired
    private PlayerRepository playerRepo;
    @Autowired
    private MongoDbFactory mongo;
    private List<Player> players;
    private int[] scores = new int[]{18, 120, 23, -72, 48, 27, 36, 22, -96, 46, 33};

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

    private boolean isPlayerGroupSavedWithMatchesAndRounds = false;

    final private int NUMBER_OF_MATCHES = 96;

    @Before
    public void savePlayerGroupAndPlayers() {
        playerGroupRepository.deleteAll();
        playerRepo.deleteAll();
        players = playerRepo.save(players);
        Player creator = players.get(0);
        PlayerGroup group = new PlayerGroup(TESTGROUP_1_NAME);
        group.setCreatedBy(creator);
        group.setPlayers(players);
        playerGroupRepository.save(group);
        int sizeAfter = playerGroupRepository.findAll().size();
        assertThat(Integer.valueOf(sizeAfter)).isEqualTo(1);
    }

    @Test
    public void testUpdatePlayerAndCheckThatReferenceIsUpdatedToo() {
        List<PlayerGroup> groups = playerGroupRepository.findAllByName(TESTGROUP_1_NAME);
        PlayerGroup group = groups.get(0);
        String gID = group.getId();
        Player creator = group.getCreatedBy();
        String newNickName = "new nickname";
        creator.setNickName(newNickName);
        creator = playerRepo.save(creator);
        group = playerGroupRepository.findOne(gID);
        assertThat(group.getCreatedBy().getNickName()).isEqualTo(creator.getNickName());
        assertThat(group.getCreatedBy().getUserID()).isEqualTo(creator.getUserID());
        assertThat(group.getPlayers()).filteredOn("nickName", newNickName).size().isEqualTo(1);
    }

    @Test
    public void testCascadingUpdateOfSubdocuments() {
        //TODO test that subdocuments are updated when document (playergroup) is saved
    }

    @Test
    public void testUpdateRounds() {

        savePlayerGroupWithMatchesAndRounds();

        PlayerGroup group = playerGroupRepository.findAllByName(TESTGROUP_1_NAME).get(0);
        Match lastMatch = group.getMatches().get(group.getMatches().size()-1);
        int numberOfRounds = lastMatch.getRounds().size();
        Round roundToBeRemoved = lastMatch.getRounds().get(0);
        Player player = roundToBeRemoved.getDeclarer();
        int player1TotalScore = lastMatch.getTotalScoreForPlayer(player);
        assertThat(numberOfRounds).isGreaterThan(0);

        lastMatch.getRounds().remove(roundToBeRemoved);
        group = playerGroupRepository.save(group);
        lastMatch = group.getMatches().get(group.getMatches().size()-1);
        assertThat(lastMatch.getRounds().size()).isEqualTo(numberOfRounds-1);
        assertThat(lastMatch.getTotalScoreForPlayer(player)).isNotEqualTo(player1TotalScore);
        //TODO implement further tests with other match index
    }

    @Test
    public void testFindAllByName() {
        List<PlayerGroup> results = playerGroupRepository.findAllByName(TESTGROUP_1_NAME);
        assertThat(results.size()).isEqualTo(1);
    }

    @Test
    public void testFindAllByPlayersUserID() {
        List<PlayerGroup> p1Groups = playerGroupRepository.findAllByPlayersUserID(players.get(0).getUserID());
        assertThat(p1Groups.size()).isEqualTo(1);
    }

    private void savePlayerGroupWithMatchesAndRounds() {

        if (isPlayerGroupSavedWithMatchesAndRounds) return;

        List<PlayerGroup> groups = playerGroupRepository.findAll();
        PlayerGroup group = groups.get(0);

        for (int a = 0; a < NUMBER_OF_MATCHES; a++) {
            Match match = new Match();
            int day = 14;
            int month = ( a % 12 ) + 1;
            int year = 2000 + (int) Math.floor(a / 12);
            match.setDate(LocalDate.of(year, month, day));
            for (int i = 0; i < 50; i++) {
                match.saveRound(
                        new Round(
                                players.get(new Random().nextInt(3)),
                                scores[new Random().nextInt(scores.length)])
                );
            }
            group.saveMatch(match);
        }
        group = playerGroupRepository.save(group);
        assertThat(group.getMatches().size()).isEqualTo(NUMBER_OF_MATCHES);
        isPlayerGroupSavedWithMatchesAndRounds = true;
    }

}
