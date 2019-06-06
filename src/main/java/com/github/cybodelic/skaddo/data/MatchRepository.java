package com.github.cybodelic.skaddo.data;

import com.github.cybodelic.skaddo.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MatchRepository extends JpaRepository<Match, Long> {
}
