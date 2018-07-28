package com.github.cybodelic.skaddo.controller;

import ch.qos.logback.classic.Logger;
import com.github.cybodelic.skaddo.data.PlayerRepository;
import com.github.cybodelic.skaddo.domain.Player;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(value = "/players")
@EnableAutoConfiguration
public class PlayerRestController {

    Logger logger = (Logger) LoggerFactory.getLogger(PlayerRestController.class);

    private final PlayerRepository repo;

    @Autowired
    PlayerRestController(PlayerRepository repo) {
        this.repo = repo;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestBody Player player) {
        if (null == player.getUserID())
            throw new InvalidDataException("mandatory attribute 'userID' not provided");

        this.repo.save(player);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(player.getUserID()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{userID}")
    Player getPlayer(@PathVariable String userID) {
        Optional<Player> p = Optional.ofNullable(repo.findOne(userID));
        if (p.isPresent())
            return p.get();
        else throw new PlayerNotFoundException(userID);
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Player> getPlayers() {
        return repo.findAll();
    }
}
