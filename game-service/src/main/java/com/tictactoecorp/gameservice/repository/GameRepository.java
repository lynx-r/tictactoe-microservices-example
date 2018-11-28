package com.tictactoecorp.gameservice.repository;

import com.tictactoecorp.gameservice.model.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * User: aleksey
 * Date: 28/11/2018
 * Time: 08:55
 */
public interface GameRepository extends ReactiveMongoRepository<Game, String> {
}
