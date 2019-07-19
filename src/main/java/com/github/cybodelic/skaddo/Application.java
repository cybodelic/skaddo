package com.github.cybodelic.skaddo;

import com.github.cybodelic.skaddo.handlers.MatchEventHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    //TODO implement in a separate class: https://stackoverflow
    // .com/questions/23697547/spring-data-rest-integration-with-spring-hateoas

    /**
     * @Bean public ResourceProcessor<Resource<Match>> matchProcessor() {
     * <p>
     * return new ResourceProcessor<Resource<Match>>() {
     * @Override public Resource<Match> process(Resource<Match> resource) {
     * MatchResource matchResource = new MatchResource(resource.getContent());
     * matchResource.add(entityLinks.linkToSingleResource(Match.class,
     * resource.getContent().getId()).withSelfRel());
     * return matchResource;
     * }
     * };
     * }
     */

    @Bean
    MatchEventHandler matchEventHandler() {
        return new MatchEventHandler();
    }
}
