package com.tictactoecorp.gameservice.service;

import com.tictactoecorp.domain.Game;
import com.tictactoecorp.domain.User;
import com.tictactoecorp.gameservice.repository.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

/**
 * User: aleksey
 * Date: 28/11/2018
 * Time: 08:51
 */
@Service
public class GameService {

  private final GameRepository gameRepository;

  public GameService(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  public Mono<Game> createGame(User userBlack, User userWhite, Boolean black) {
    Game game;
    if (black) {
      game = new Game(userBlack, userWhite);
    } else {
      game = new Game(userWhite, userBlack);
    }
    initGameField(game);
    return gameRepository.save(game);
  }

  private void initGameField(Game game) {
    game.setField(new ArrayList<>(Game.FIELD_SIZE));
    for (int i = 0; i < Game.FIELD_SIZE; i++) {
      var row = new ArrayList<Boolean>(Game.FIELD_SIZE);
      for (int j = 0; j < Game.FIELD_SIZE; j++) {
        row.add(null);
      }
      game.getField().add(row);
    }
  }
}
