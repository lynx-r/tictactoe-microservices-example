package com.tictactoe.webapi.controller;

import com.tictactoe.domain.Game;
import com.tictactoe.domain.User;
import com.tictactoe.webapi.service.WebApiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 08:49
 */
@RestController
public class WebApiController {

  private final WebApiService webApiService;

  public WebApiController(WebApiService webApiService) {
    this.webApiService = webApiService;
  }

  @GetMapping("greetGuest")
  @PreAuthorize("permitAll()")
  public Mono<Map> getIndex() {
    return Mono.just(Map.of("greet", "Hi there"));
  }

  @GetMapping("greetAdmin")
  @PreAuthorize("hasRole('ADMIN')")
  public Mono<Map> getIndexProtected() {
    return Mono.just(Map.of("greet", "Hi admin"));
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
