package com.github.cybodelic.skaddo.repositories;

import com.github.cybodelic.skaddo.domain.Player;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "users", itemResourceRel = "user", path = "users")
public interface PlayerRepository extends PagingAndSortingRepository<Player, String> {

    List<Player> findByNickNameIgnoreCaseStartingWith(@Param("nickName") String nickName);

    List<Player> findByUserIDIgnoreCaseStartingWith(@Param("userID") String userID);

    List<Player> findByComposedNameIgnoreCaseContaining(@Param("composedName") String composedName);

    List<Player> findByFirstNameIgnoreCaseStartingWith(@Param("firstName") String firstName);

    List<Player> findByLastNameIgnoreCaseStartingWith(@Param("lastName") String lastName);

}
