package com.github.cybodelic.skaddo.data;

import com.github.cybodelic.skaddo.domain.Player;
import com.github.cybodelic.skaddo.domain.PlayerGroup;
import com.mongodb.DB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerGroupRepositoryTest {

    @Autowired
    private PlayerGroupRepository playerGroupRepository;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private MongoDbFactory mongo;

    final private String TESTGROUP_1_NAME = "Demo group";

    private List<Player> players;

    {
        players = new ArrayList<Player>();
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        players.add(new Player("Player 3"));
    }

    @After
    public void printCollections() {
        DB db = mongo.getDb();
        System.out.println("DB " + db.getName() + " contains collections: ");
        for (String collection : db.getCollectionNames()) {
            System.out.println(collection);
        }
    }

    @Before
    public void testSaveWithPlayers() {
        mongo.getDb().dropDatabase();
        int sizeBefore = playerGroupRepository.findAll().size();
        playerRepo.save(players);
        PlayerGroup group = new PlayerGroup(TESTGROUP_1_NAME);
        group.setPlayers(players);
        playerGroupRepository.save(group);
        int sizeAfter = playerGroupRepository.findAll().size();
        assertThat(Integer.valueOf(sizeAfter)).isEqualTo(++sizeBefore);
    }

    @Test
    public void testSaveWithMatches() {
        //TODO implement test
    }

    @Test
    public void testSaveWithMatchesAndRounds() {
        //TODO implement test
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

}
