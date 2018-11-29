package com.tictactoecorp.gameservice.client;

import com.tictactoecorp.domain.User;
import com.tictactoecorp.gameservice.config.ApplicationConfig;
import org.springframework.http.MediaType;
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
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .retrieve()
        .bodyToMono(User.class);
  }
}
