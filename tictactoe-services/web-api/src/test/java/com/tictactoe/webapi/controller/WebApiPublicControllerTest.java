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

/**
 * User: aleksey
 * Date: 2018-12-04
 * Time: 08:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestConfig.class)
public class WebApiPublicControllerTest {

  @Autowired
  private TestConfig testConfig;
  @Autowired
  private WebApiPublicController webApiPublicController;
  private WebTestClient webTestClient;

  @Before
  public void setUp() throws Exception {
    webTestClient = WebTestClient.bindToController(webApiPublicController).build();
  }

  @Test
  public void greet() {
    webTestClient
        .get()
        .uri("/public/greet")
        .exchange()
        .expectStatus().isOk()
        .expectBody().json("{\"greet\":\"Hi everybody\"}");
  }
}