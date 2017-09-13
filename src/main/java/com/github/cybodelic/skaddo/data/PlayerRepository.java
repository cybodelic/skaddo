package com.github.cybodelic.skaddo.data;

import com.github.cybodelic.skaddo.domain.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, String>{

    Player findByUserID(String userID);
}
