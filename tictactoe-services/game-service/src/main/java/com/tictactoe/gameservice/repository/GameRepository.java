package com.tictactoe.gameservice.repository;

import com.tictactoe.domain.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * User: aleksey
 * Date: 28/11/2018
 * Time: 08:55
 */
public interface GameRepository extends ReactiveMongoRepository<Game, String> {
}
