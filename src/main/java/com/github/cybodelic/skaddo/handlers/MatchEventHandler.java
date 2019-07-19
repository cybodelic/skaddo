package com.github.cybodelic.skaddo.handlers;

import com.github.cybodelic.skaddo.repositories.MatchRepository;
import com.github.cybodelic.skaddo.repositories.PlayerGroupRepository;
import com.github.cybodelic.skaddo.domain.Match;
import com.github.cybodelic.skaddo.domain.PlayerGroup;
import com.github.cybodelic.skaddo.domain.Round;
import com.github.cybodelic.skaddo.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterLinkDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.HandleBeforeLinkSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RepositoryEventHandler
public class MatchEventHandler {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerGroupRepository groupRepository;

    @HandleAfterLinkSave
    @HandleAfterLinkDelete
    public void afterRoundAddedOrDeleted(Match match, List<Round> rounds) {
        Round newRound = rounds.get(rounds.size() - 1);
        match.setScores(updateScoresMap(match.getScores(), newRound));
        this.matchRepository.save(match);
        PlayerGroup group = groupRepository.findByMatchesId(match.getId());
        group.setScores(updateScoresMap(group.getScores(), newRound));
        this.groupRepository.save(group);

    }

    @HandleBeforeLinkSave
    public void beforeRoundAdded(Match match, List<Round> rounds) {
        PlayerGroup group = groupRepository.findByMatchesId(match.getId());
        if (group == null) {
            throw new InvalidDataException("Need to add match " + match.getId()
                    + " to a playergroup before you can  add  rounds to it.");
        }
    }

    private Map<String, Integer> updateScoresMap(Map<String, Integer> scores, Round round) {
        if ( scores == null ) {
            scores = new HashMap<>();
        }
        String declarerId = round.getDeclarer().getUserID();
        scores.putIfAbsent(declarerId, 0);
        scores.put(declarerId, round.getScore() + scores.get(declarerId));
        return scores;
    }
}
