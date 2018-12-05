package com.tictactoe.gameservice.client;

import com.tictactoe.domain.User;
import com.tictactoe.gameservice.config.ApplicationConfig;
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
  private final String userServiceUrl;

  public UserWebClient(WebClient.Builder webClientBuilder,
                       ApplicationConfig applicationConfig) {
    this.webClientBuilder = webClientBuilder;
    this.userServiceUrl = applicationConfig.getUserServiceUrl();
  }

  public Mono<User> getUser(String userId) {
    return webClientBuilder
        .build()
        .get()
        .uri(userServiceUrl + "/v1/users/{userId}", userId)
        .retrieve()
//        .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(new RuntimeException("ERROR 4xx: " + resp.body(BodyExtractors.toMono(Map.class)).toString())))
//        .onStatus(HttpStatus::is5xxServerError, resp -> Mono.error(new RuntimeException("5xx")))
        .bodyToMono(User.class);
  }
}
