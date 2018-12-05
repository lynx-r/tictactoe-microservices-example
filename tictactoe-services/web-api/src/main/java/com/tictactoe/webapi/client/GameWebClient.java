package com.tictactoe.webapi.client;

import com.tictactoe.domain.Game;
import com.tictactoe.webapi.config.ApplicationConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 07:05
 */
@Service
public class GameWebClient {

  private final WebClient.Builder webClientBuilder;
  private final String gameServiceUrl;

  public GameWebClient(WebClient.Builder webClientBuilder,
                       ApplicationConfig applicationConfig) {
    this.webClientBuilder = webClientBuilder;
    this.gameServiceUrl = applicationConfig.getGameServiceUrl();
  }

  public Flux<Game> getAllGames() {
    return webClientBuilder
        .build()
        .get()
        .uri(gameServiceUrl + "/v1/games")
        .retrieve()
        .bodyToFlux(Game.class);
  }

  public Mono<Game> createGame(Map<String, Object> params) {
    Boolean black = (Boolean) params.remove("black");
    return webClientBuilder
        .build()
        .post()
        .uri(uriBuilder ->
            new DefaultUriBuilderFactory(gameServiceUrl)
                .builder()
                .path("/v1/games/{userFirst}/{userSecond}")
                .queryParam("black", black)
                .build(params)
        )
        .retrieve()
        .bodyToMono(Game.class);
  }

  public Mono<Game> getGame(String gameId) {
    return webClientBuilder
        .build()
        .get()
        .uri(gameServiceUrl + "/v1/games/{gameId}", gameId)
        .retrieve()
//        .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(new RuntimeException("4xx")))
//        .onStatus(HttpStatus::is5xxServerError, resp -> Mono.error(new RuntimeException("5xx")))
        .bodyToMono(Game.class);
  }
}
