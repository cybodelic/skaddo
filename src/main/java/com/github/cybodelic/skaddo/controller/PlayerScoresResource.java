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
}
