package com.github.cybodelic.skaddo.repositories;

import com.github.cybodelic.skaddo.domain.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RoundRepository extends JpaRepository<Round, Long> {
}
