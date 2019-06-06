package com.github.cybodelic.skaddo.controller;

import com.github.cybodelic.skaddo.data.MatchRepository;
import com.github.cybodelic.skaddo.domain.Match;
import com.github.cybodelic.skaddo.domain.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterLinkDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import java.util.List;

@RepositoryEventHandler
public class MatchEventHandler {

    @Autowired
    private MatchRepository matchRepo;

    @HandleAfterLinkSave
    @HandleAfterLinkDelete
    public void handleLinkSaveOrDelete(Match match, List<Round> rounds) {
        match.recalculatePlayerScores();
        this.matchRepo.save(match);

    }
}
