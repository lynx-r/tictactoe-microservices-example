package com.tictactoecorp.gameservice.controller;

import com.tictactoecorp.gameservice.service.GameService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.ws.rs.QueryParam;

/**
 * User: aleksey
 * Date: 28/11/2018
 * Time: 08:49
 */
@RestController
@RequestMapping("v1/game/{userId}")
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("/{opponentId}")
  public Mono<?> createGame(
      @PathVariable("userId") String userId,
      @PathVariable("opponentId") String opponentId,
      @QueryParam("black") Boolean black
  ) {
    return gameService.createGame(userId, opponentId, black);
  }
}
