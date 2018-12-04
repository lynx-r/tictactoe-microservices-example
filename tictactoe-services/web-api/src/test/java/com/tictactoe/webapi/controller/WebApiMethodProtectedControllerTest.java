package com.tictactoe.webapi.controller;

import com.tictactoe.domain.User;
import com.tictactoe.webapi.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.tictactoe.webapi.util.TestUtils.basicAuthHeaders;
import static com.tictactoe.webapi.util.TestUtils.tokenAuthHeaders;

/**
 * User: aleksey
 * Date: 2018-12-04
 * Time: 09:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestConfig.class)
public class WebApiMethodProtectedControllerTest {

  @Autowired
  private TestConfig testConfig;
  private WebTestClient webTestClient;

  @Before
  public void setUp() throws Exception {
    webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:40010").build();
  }

  @Test
  public void greetAdmin_BaseAuth__Admin_Ok() {
    webTestClient
        .get()
        .uri("/method-protected/greetAdmin")
        .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void greetAdmin_BaseAuth_User_Forbidden() {
    webTestClient
        .get()
        .uri("/method-protected/greetAdmin")
        .headers(basicAuthHeaders(testConfig.getUserName(), testConfig.getUserPassword()))
        .exchange()
        .expectStatus().isForbidden();
  }

  @Test
  public void getAllUsers_BaseAuth_Admin_Ok() {
    webTestClient
        .get()
        .uri("/method-protected/users")
        .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void getAllUsers_BaseAuth_Anonymous_Unauthorized() {
    webTestClient
        .get()
        .uri("/method-protected/users")
        .headers(basicAuthHeaders(testConfig.getAdminName(), ""))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  public void createUser_BaseAuth_Admin_Ok() {
    webTestClient
        .post()
        .uri("/method-protected/user")
        .body(BodyInserters.fromObject(getUser("user1")))
        .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void createUser_BaseAuth_User_Forbidden() {
    webTestClient
        .post()
        .uri("/method-protected/user")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(BodyInserters.fromObject(getUser("user1")))
        .headers(basicAuthHeaders(testConfig.getUserName(), testConfig.getUserPassword()))
        .exchange()
        .expectStatus().isForbidden();
  }

  @Test
  public void greetAdmin_TokenAuth__Admin_Ok() {
    webTestClient
        .get()
        .uri("/method-protected/greetAdmin")
        .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void greetAdmin_TokenAuth_User_Forbidden() {
    webTestClient
        .get()
        .uri("/method-protected/greetAdmin")
        .headers(tokenAuthHeaders(webTestClient, testConfig.getUserName(), testConfig.getUserPassword()))
        .exchange()
        .expectStatus().isForbidden();
  }

  @Test
  public void getAllUsers_TokenAuth_Admin_Ok() {
    webTestClient
        .get()
        .uri("/method-protected/users")
        .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void getAllUsers_TokenAuth_Anonymous_Unauthorized() {
    webTestClient
        .get()
        .uri("/method-protected/users")
        .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), ""))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  public void createUser_TokenAuth_Admin_Ok() {
    webTestClient
        .post()
        .uri("/method-protected/user")
        .body(BodyInserters.fromObject(getUser("user1")))
        .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), testConfig.getAdminPassword()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void createUser_TokenAuth_User_Forbidden() {
    webTestClient
        .post()
        .uri("/method-protected/user")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(BodyInserters.fromObject(getUser("user1")))
        .headers(tokenAuthHeaders(webTestClient, testConfig.getUserName(), testConfig.getUserPassword()))
        .exchange()
        .expectStatus().isForbidden();
  }

  private User getUser(String userName) {
    return User.builder().name(userName).build();
  }
}