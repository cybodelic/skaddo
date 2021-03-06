package com.github.cybodelic.skaddo.repositories;

import com.github.cybodelic.skaddo.domain.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest

public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository repository;

    private List<Player> players;

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

    @Test
    public void testDuplicateUsernameException() {
        repository.deleteAll();
        repository.save(players.get(0));
        //assertThat(repository.findAll().size()).isEqualTo(1);
        assertThatExceptionOfType(Exception.class).isThrownBy(
                () -> repository.save(players.get(0))).withMessageContaining("duplicate");
    }

    @Test
    public void testFindByUserID() {
        repository.saveAll(players);
        Player p1 = repository.findById(players.get(0).getUserID()).get();
        assertThat(p1.getUserID()).isEqualTo(players.get(0).getUserID());

        String additionalPlayerUserID = "Additional.Player@whatsoever.foo";
        Player additionalPlayer = new Player(additionalPlayerUserID);
        additionalPlayer.setNickName("addy");
        repository.save(additionalPlayer);
        Player p2 = repository.findById(additionalPlayerUserID).get();
        assertThat(p2.getUserID()).isEqualTo(additionalPlayerUserID);
    }

    @Test
    public void testNickNameNotNullable() {
        Player nicklessPlayer = new Player("userid");
        assertThatThrownBy(() -> repository.save(nicklessPlayer)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void testFindByUserIDIgnoreCaseStartingWith() {
        repository.saveAll(players);
        List<Player> result = repository.findByUserIDIgnoreCaseStartingWith("Player");
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void findByNickNameIgnoreCaseStartingWith() {
        repository.saveAll(players);
        List<Player> result = repository.findByNickNameIgnoreCaseStartingWith("nick ");
        assertThat(result.size()).isEqualTo(1);

        result = repository.findByNickNameIgnoreCaseStartingWith("xxx ");
        assertThat(result.size()).isEqualTo(0);

        result = repository.findByNickNameIgnoreCaseStartingWith("ick #2");
        assertThat(result.size()).isEqualTo(0);

        result = repository.findByNickNameIgnoreCaseStartingWith("nick");
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    public void findByFirstNameIgnoreCaseStartingWith() {
        repository.saveAll(players);
        List<Player> result = repository.findByFirstNameIgnoreCaseStartingWith("First");
        assertThat(result.size()).isEqualTo(2);

        result = repository.findByFirstNameIgnoreCaseStartingWith("firstNamE");
        assertThat(result.size()).isEqualTo(2);

        result = repository.findByFirstNameIgnoreCaseStartingWith("x");
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void findByLastNameIgnoreCaseStartingWith() {
        repository.saveAll(players);
        List<Player> result = repository.findByLastNameIgnoreCaseStartingWith("Last");
        assertThat(result.size()).isEqualTo(2);

        result = repository.findByLastNameIgnoreCaseStartingWith("lAsTnAme");
        assertThat(result.size()).isEqualTo(2);

        result = repository.findByFirstNameIgnoreCaseStartingWith("x");
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void findByComposedNameIgnoreCaseContaining() {
        repository.deleteAll();
        repository.saveAll(players);
        List<Player> result = repository.findByComposedNameIgnoreCaseContaining("name-");
        assertThat(result.size()).isEqualTo(1);

        result = repository.findByComposedNameIgnoreCaseContaining("Name-");
        assertThat(result.size()).isEqualTo(1);

        result = repository.findByComposedNameIgnoreCaseContaining("Name");
        assertThat(result.size()).isEqualTo(2);

        result = repository.findByComposedNameIgnoreCaseContaining("lAsTnAmE");
        assertThat(result.size()).isEqualTo(2);
    }

}
