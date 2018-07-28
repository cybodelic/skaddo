package com.github.cybodelic.skaddo.data;

import com.github.cybodelic.skaddo.domain.Player;
import com.github.cybodelic.skaddo.domain.PlayerGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerGroupRepository extends MongoRepository<PlayerGroup, String> {

    List<PlayerGroup> findAllByName(String name);

    List<PlayerGroup> findAllByPlayersUserID(String userID);
}
