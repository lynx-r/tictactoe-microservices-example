package com.tictactoe.webapi.client;

import com.tictactoe.domain.Game;
import com.tictactoe.webapi.config.ApplicationConfig;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 07:05
 */
@Service
public class GameWebClient {

  private final WebClient.Builder webClientBuilder;
  private final ApplicationConfig applicationConfig;

  public GameWebClient(WebClient.Builder webClientBuilder,
                       ApplicationConfig applicationConfig) {
    this.webClientBuilder = webClientBuilder;
    this.applicationConfig = applicationConfig;
  }

  public Mono<Game> getGame(String gameId) {
    return webClientBuilder
        .build()
        .get()
        .uri(applicationConfig.getGameServiceUrl() + "/v1/game/{gameId}", gameId)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(new RuntimeException("4xx")))
        .onStatus(HttpStatus::is5xxServerError, resp -> Mono.error(new RuntimeException("5xx")))
        .bodyToMono(Game.class);
  }
}
