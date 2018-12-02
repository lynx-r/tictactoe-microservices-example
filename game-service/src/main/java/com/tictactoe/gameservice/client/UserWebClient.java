package com.tictactoe.gameservice.client;

import com.tictactoe.domain.User;
import com.tictactoe.gameservice.config.ApplicationConfig;
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
public class UserWebClient {

  private final WebClient.Builder webClientBuilder;
  private final ApplicationConfig applicationConfig;

  public UserWebClient(WebClient.Builder webClientBuilder,
                       ApplicationConfig applicationConfig) {
    this.webClientBuilder = webClientBuilder;
    this.applicationConfig = applicationConfig;
  }

  public Mono<User> getUser(String userId) {
    return webClientBuilder
        .build()
        .get()
        .uri(applicationConfig.getUserServiceUrl() + "/v1/users/{userId}", userId)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(new RuntimeException("4xx")))
        .onStatus(HttpStatus::is5xxServerError, resp -> Mono.error(new RuntimeException("5xx")))
        .bodyToMono(User.class);
  }
}
