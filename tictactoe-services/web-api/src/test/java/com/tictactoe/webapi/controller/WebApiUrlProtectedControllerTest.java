package com.tictactoe.webapi.controller;

import com.tictactoe.domain.User;
import com.tictactoe.webapi.config.TestConfig;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tictactoe.webapi.config.TestConstants.*;
import static com.tictactoe.webapi.util.TestUtils.basicAuthHeaders;
import static com.tictactoe.webapi.util.TestUtils.tokenAuthHeaders;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
  private WebClient webClient;

  @Before
  public void setUp() {
    webTestClient = WebTestClient.bindToServer().baseUrl(WEB_API_BASE_URL).build();
    webClient = WebClient.builder().baseUrl(WEB_API_BASE_URL).build();
  }

  @Test
  public void getAllGames_BaseAuth_Admin_Ok() {
    webTestClient
        .get()
        .uri(GAMES_URL)
        .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void getAllGames_BaseAuth_Anonymous_Unauthorized() {
    webTestClient
        .get()
        .uri(GAMES_URL)
        .headers(basicAuthHeaders(testConfig.getAdminName(), ""))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  public void createGame_BaseAuth_Admin_Ok() {
    List<User> users = webClient
        .get()
        .uri(USERS_URL)
        .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
        .retrieve()
        .bodyToFlux(User.class)
        .collect(Collectors.toList())
        .block();
    assertNotNull(users);
    assertTrue(users.size() > 3);
    webTestClient
        .post()
        .uri(GAME_URL)
        .body(BodyInserters.fromObject(createGame(users.get(0), users.get(1))))
        .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void createGame_BaseAuth_User_Forbidden() {
    List<User> users = webClient
        .get()
        .uri(USERS_URL)
        .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
        .retrieve()
        .bodyToFlux(User.class)
        .collect(Collectors.toList())
        .block();
    assertNotNull(users);
    assertTrue(users.size() > 3);
    webTestClient
        .post()
        .uri(GAME_URL)
        .body(BodyInserters.fromObject(createGame(users.get(0), users.get(1))))
        .headers(basicAuthHeaders(testConfig.getUserName(), testConfig.getUserPassword()))
        .exchange()
        .expectStatus().isForbidden();
  }

  @Test
  public void getAllGames_TokenAuth_Admin_Ok() {
    webTestClient
        .get()
        .uri(GAMES_URL)
        .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void getAllGames_TokenAuth_Anonymous_Unauthorized() {
    webTestClient
        .get()
        .uri(GAMES_URL)
        .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), ""))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  public void createGame_TokenAuth_Admin_Ok() {
    List<User> users = webClient
        .get()
        .uri(USERS_URL)
        .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), testConfig.getAdminPassword()))
        .retrieve()
        .bodyToFlux(User.class)
        .collect(Collectors.toList())
        .block();
    assertNotNull(users);
    assertTrue(users.size() > 3);
    webTestClient
        .post()
        .uri(GAME_URL)
        .body(BodyInserters.fromObject(createGame(users.get(0), users.get(1))))
        .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void createGame_TokenAuth_User_Forbidden() {
    List<User> users = webClient
        .get()
        .uri(USERS_URL)
        .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), testConfig.getAdminPassword()))
        .retrieve()
        .bodyToFlux(User.class)
        .collect(Collectors.toList())
        .block();
    assertNotNull(users);
    assertTrue(users.size() > 3);
    webTestClient
        .post()
        .uri(GAME_URL)
        .body(BodyInserters.fromObject(createGame(users.get(0), users.get(1))))
        .headers(tokenAuthHeaders(webTestClient, testConfig.getUserName(), testConfig.getUserPassword()))
        .exchange()
        .expectStatus().isForbidden();
  }

  private Map createGame(User userBlack, User userWhite) {
    return Map.of(
        "userFirst", userBlack.getId(),
        "userSecond", userWhite.getId(),
        "black", RandomUtils.nextBoolean());
  }
}