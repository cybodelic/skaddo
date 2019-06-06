package com.github.cybodelic.skaddo.controller;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

public class PlayerScoresResource extends ResourceSupport {

    private List<PlayerScore> scores = new ArrayList<>();

    public List<PlayerScore> getScores() {
        return scores;
    }

    public void setScores(List<PlayerScore> scores) {
        this.scores = scores;
    }

    /**
     * Creates a PlayerScoreResource from the results of a query.
     * @param queryResult Each object in this list must have the playerId at [0] and the sum of the
     *                    scores at index [1]
     * @return A PlayerScoreResource populated with the values from the query
     */
    PlayerScoresResource createFrom(List<Object[]> queryResult) {

        PlayerScoresResource resource = new PlayerScoresResource();

        queryResult.forEach(a -> {
            PlayerScore s = new PlayerScore((String) a[0], (long) a[1]);
            resource.getScores().add(s);
        });

        return resource;
    }
}
