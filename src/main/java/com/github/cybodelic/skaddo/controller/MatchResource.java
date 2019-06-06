package com.github.cybodelic.skaddo.controller;

import com.github.cybodelic.skaddo.domain.Match;
import org.springframework.hateoas.Resource;

public class MatchResource extends Resource<Match> {

    private PlayerScoresResource playerScores;

    public MatchResource(Match match) {
        super(match);
    }

    public PlayerScoresResource getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(PlayerScoresResource playerScores) {
        this.playerScores = playerScores;
    }
}
