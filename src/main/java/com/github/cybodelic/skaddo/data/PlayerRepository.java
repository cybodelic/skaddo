package com.github.cybodelic.skaddo.data;

import com.github.cybodelic.skaddo.domain.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, String>{

    List<Player> findByNickNameIgnoreCaseStartingWith(String nickName);

    List<Player> findByUserIDIgnoreCaseStartingWith(String userID);

    List<Player> findByComposedNameIgnoreCaseContaining(String composedName);

    List<Player> findByFirstNameIgnoreCaseStartingWith(String firstName);

    List<Player> findByLastNameIgnoreCaseStartingWith(String lastName);

}
