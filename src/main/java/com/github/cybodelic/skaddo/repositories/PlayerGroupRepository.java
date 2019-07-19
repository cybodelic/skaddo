package com.github.cybodelic.skaddo.repositories;

import com.github.cybodelic.skaddo.domain.PlayerGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource
public interface PlayerGroupRepository extends PagingAndSortingRepository<PlayerGroup, Long> {

    List<PlayerGroup> findAllByName(@Param("name") String name);

    List<PlayerGroup> findByNameLike(@Param("name") String name);

    List<PlayerGroup> findAllByPlayersUserID(@Param("userId") String userId);

    PlayerGroup findByMatchesId(@Param("matchId") Long matchId);

    @RestResource(exported = false)
    @Query(value = "SELECT COUNT(m) from PlayerGroup g JOIN g.matches m WHERE g.id = :id")
    int countMatches(@Param("id") String id);

    @RestResource(exported = false)
    @Query(value = "SELECT SUM(r.score) from PlayerGroup g JOIN g.matches m JOIN m.rounds r WHERE" +
            " g.id = :groupId AND r.declarer.id = :playerId")
    int getPlayerScore(@Param("groupId") String groupId, @Param("playerId") String playerId);

    @RestResource(exported = false)
    @Query(value = "SELECT r.declarer.id, SUM(r.score) from PlayerGroup g JOIN g.matches m JOIN m" +
            ".rounds r WHERE g.id = :groupId GROUP BY r.declarer.id")
    List<Object[]> getPlayerGroupScores(@Param("groupId") Long groupId);
}
