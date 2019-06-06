package com.github.cybodelic.skaddo.controller;

import com.github.cybodelic.skaddo.data.PlayerGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class PlayerGroupScoresController {

    private final PlayerGroupRepository repository;

    @Autowired
    public PlayerGroupScoresController(PlayerGroupRepository repo) {
        repository = repo;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/playerGroups/{groupId}/scores")
    public @ResponseBody
    ResponseEntity<?> getScores(@PathVariable Long groupId) {

        PlayerScoresResource resource = new PlayerScoresResource()
                .createFrom(repository.getPlayerGroupScores(groupId));

        resource.add(linkTo(methodOn(PlayerGroupScoresController.class).getScores(groupId))
                .withSelfRel());

        return ResponseEntity.ok(resource);
    }
}
