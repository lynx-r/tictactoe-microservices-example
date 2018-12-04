package com.tictactoe.webapi.controller;

import com.tictactoe.webapi.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.tictactoe.webapi.util.TestUtils.basicAuthHeaders;

/**
 * User: aleksey
 * Date: 2018-12-04
 * Time: 06:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestConfig.class)
public class DefaultControllerTest {

  @Autowired
  private DefaultController defaultController;
  private WebTestClient webTestClient;

  @Before
  public void setUp() throws Exception {
    webTestClient = WebTestClient.bindToController(defaultController).build();
  }

  @Test
  public void guestHome() {
    webTestClient.get()
        .uri("/")
        .exchange()
        .expectStatus().isOk()
        .expectBody().json("{\"greet\":\"Welcome Guest\"}");
  }

  @Test
  public void login() {
    webTestClient.get()
        .uri("/login")
        .exchange()
        .expectStatus().isOk()
        .expectBody().json("{\"greet\":\"Welcome Guest, its Basic Authentication\"}");
  }

  @Test
  public void loginSuccess() {
    webTestClient.get()
        .uri("/login")
        .headers(basicAuthHeaders("admin", "admin"))
        .exchange()
        .expectStatus().isOk()
        .expectBody().json("{\"greet\":\"Welcome Guest, its Basic Authentication\"}");
  }

  @Test
  public void loginFail() {
    webTestClient.get()
        .uri("/login")
        .headers(basicAuthHeaders("admin", ""))
        .exchange()
        .expectStatus().isOk()
        .expectBody().json("{\"greet\":\"Welcome Guest, its Basic Authentication\"}");
  }
}