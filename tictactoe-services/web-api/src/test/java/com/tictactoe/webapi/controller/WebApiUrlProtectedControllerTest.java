package com.tictactoe.webapi.controller;

import com.tictactoe.domain.Game;
import com.tictactoe.domain.User;
import com.tictactoe.webapi.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.tictactoe.webapi.util.TestUtils.basicAuthHeaders;

/**
 * User: aleksey
 * Date: 2018-12-04
 * Time: 08:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestConfig.class)
public class WebApiUrlProtectedControllerTest {

  @Autowired
  private TestConfig testConfig;
  private WebTestClient webTestClient;

  @Before
  public void setUp() throws Exception {
    webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:40010").build();
  }

  @Test
  public void getAllGamesAuth() {
    webTestClient
        .get()
        .uri("/url-protected/games")
        .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void getAllGamesNotAuth() {
    webTestClient
        .get()
        .uri("/url-protected/games")
        .headers(basicAuthHeaders(testConfig.getAdminName(), ""))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  public void createGamesAuth() {
    webTestClient
        .post()
        .uri("/url-protected/game")
        .body(BodyInserters.fromObject(new Game(new User("userBlack", 0), new User("userWhite", 0))))
        .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void createGame() {
  }
}