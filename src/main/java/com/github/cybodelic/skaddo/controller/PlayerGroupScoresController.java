package com.github.cybodelic.skaddo.controller;

import com.github.cybodelic.skaddo.data.PlayerGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class PlayerGroupScoresController {

    private final PlayerGroupRepository groupRepository;

    @Autowired
    public PlayerGroupScoresController(PlayerGroupRepository repo) {
        groupRepository = repo;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/playerGroups/{groupId}/scores")
    public @ResponseBody
    ResponseEntity<?> getScores(@PathVariable String groupId) {

        List<Object[]> scores = groupRepository.getPlayerGroupScores(groupId);

        PlayerScoresResource resource = new PlayerScoresResource();

        scores.forEach(a -> {
            PlayerScore s = new PlayerScore((String) a[0], (long) a[1]);
            resource.getScores().add(s);
        });

        resource.add(linkTo(methodOn(PlayerGroupScoresController.class).getScores(groupId)).withSelfRel());

        return ResponseEntity.ok(resource);
    }
}
