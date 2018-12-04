package com.tictactoe.webapi.util;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Map;
import java.util.function.Consumer;

/**
 * User: aleksey
 * Date: 2018-12-04
 * Time: 06:40
 */
public class TestUtils {
  public static Consumer<HttpHeaders> tokenAuthHeaders(WebTestClient webTestClient, String username, String password) {
    return (headers) -> {
      Map token = webTestClient
          .post()
          .uri("/auth/token")
          .body(BodyInserters.fromObject(Map.of("username", username, "password", password)))
          .exchange()
          .returnResult(Map.class)
          .getResponseBody()
          .blockFirst();
      headers.setBearerAuth((String) token.get("token"));
    };
  }

  public static Consumer<HttpHeaders> basicAuthHeaders(String username, String password) {
    return (headers) -> {
      headers.setBasicAuth(username, password);
    };
  }
}
