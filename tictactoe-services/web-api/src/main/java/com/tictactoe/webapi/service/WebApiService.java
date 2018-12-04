package com.tictactoe.webapi.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tictactoe.domain.Game;
import com.tictactoe.domain.User;
import com.tictactoe.webapi.client.GameWebClient;
import com.tictactoe.webapi.client.UserWebClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 08:49
 */
@Service
public class WebApiService {

  private final UserWebClient userWebClient;
  private final GameWebClient gameWebClient;

  public WebApiService(
      UserWebClient userWebClient,
      GameWebClient gameWebClient
  ) {
    this.userWebClient = userWebClient;
    this.gameWebClient = gameWebClient;
  }

  @HystrixCommand
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  public Flux<User> getAllUsers() {
    return userWebClient.getAllUsers();
  }

  @HystrixCommand
  @PreAuthorize("hasRole('ADMIN')")
  public Mono<User> createUser(User userRequest) {
    return userWebClient.createUser(userRequest);
  }

  @HystrixCommand
  public Flux<Game> getAllGames() {
    return gameWebClient.getAllGames();
  }

  @HystrixCommand
  public Mono<Game> createGame(Map<String, Object> gameRequest) {
    return gameWebClient.createGame(gameRequest);
  }
}
