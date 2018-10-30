package com.github.cybodelic.skaddo.data;

import com.github.cybodelic.skaddo.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "users", itemResourceRel = "user", path = "users")
public interface PlayerRepository extends JpaRepository<Player, String> {

    List<Player> findByNickNameIgnoreCaseStartingWith(@Param("nickName") String nickName);

    List<Player> findByUserIDIgnoreCaseStartingWith(@Param("userID") String userID);

    List<Player> findByComposedNameIgnoreCaseContaining(@Param("composedName") String composedName);

    List<Player> findByFirstNameIgnoreCaseStartingWith(@Param("firstName") String firstName);

    List<Player> findByLastNameIgnoreCaseStartingWith(@Param("lastName") String lastName);

}
