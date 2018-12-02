package com.tictactoe.webapi.controller;

import com.tictactoe.domain.Game;
import com.tictactoe.domain.User;
import com.tictactoe.webapi.service.WebApiService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 08:49
 */
@RestController
@RequestMapping("v1")
public class WebApiController {

  private final WebApiService webApiService;

  public WebApiController(WebApiService webApiService) {
    this.webApiService = webApiService;
  }

  @GetMapping("users")
  public Flux<User> getAllUsers() {
    return webApiService.getAllUsers();
  }

  @PostMapping("user")
  public Mono<User> createUser(@RequestBody User userRequest) {
    return webApiService.createUser(userRequest);
  }

  @GetMapping("games")
  public Flux<Game> getAllGames() {
    return webApiService.getAllGames();
  }

  /**
   * @param gameRequest params {userFirst: String, userSecond: String, black: boolean}.
   *                    if black == true then userFirst is black
   * @return
   */
  @PostMapping("game")
  public Mono<Game> createGame(@RequestBody Map<String, Object> gameRequest) {
    return webApiService.createGame(gameRequest);
  }
}
