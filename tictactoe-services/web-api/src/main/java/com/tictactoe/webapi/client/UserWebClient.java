package com.tictactoe.webapi.client;

import com.tictactoe.domain.User;
import com.tictactoe.webapi.config.ApplicationConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 07:05
 */
@Service
public class UserWebClient {

  private final WebClient.Builder webClientBuilder;

  public UserWebClient(WebClient.Builder webClientBuilder,
                       ApplicationConfig applicationConfig) {
    this.webClientBuilder = webClientBuilder.baseUrl(applicationConfig.getUserServiceUrl());
  }

  public Flux<User> getAllUsers() {
    return webClientBuilder
        .build()
        .get()
        .uri("/v1/users")
        .retrieve()
        .bodyToFlux(User.class);
  }

  public Mono<User> createUser(User userRequest) {
    return webClientBuilder
        .build()
        .post()
        .uri("/v1/users")
        .body(BodyInserters.fromObject(userRequest))
        .retrieve()
        .bodyToMono(User.class);
  }

  public Mono<User> getUser(String userId) {
    return webClientBuilder
        .build()
        .get()
        .uri("/v1/users/{userId}", userId)
        .retrieve()
        .bodyToMono(User.class);
  }
}
