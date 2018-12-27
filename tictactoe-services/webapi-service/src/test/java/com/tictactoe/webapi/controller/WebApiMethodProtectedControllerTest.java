/*
 * © Copyright 2018 Aleksey Popryadukhin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tictactoe.webapi.controller;

import com.tictactoe.domain.User;
import com.tictactoe.webapi.config.TestConfig;
import org.apache.commons.lang.RandomStringUtils;
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

import static com.tictactoe.webapi.config.TestConstants.*;
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
    public void setUp() {
        webTestClient = WebTestClient.bindToServer().baseUrl(WEB_API_BASE_URL).build();
    }

    @Test
    public void greetAdmin_BaseAuth__Admin_Ok() {
        webTestClient
                .get()
                .uri(GREET_ADMIN_URL)
                .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void greetAdmin_BaseAuth_User_Forbidden() {
        webTestClient
                .get()
                .uri(GREET_ADMIN_URL)
                .headers(basicAuthHeaders(testConfig.getUserName(), testConfig.getUserPassword()))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void getAllUsers_BaseAuth_Admin_Ok() {
        webTestClient
                .get()
                .uri(USERS_URL)
                .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void getAllUsers_BaseAuth_Anonymous_Unauthorized() {
        webTestClient
                .get()
                .uri(USERS_URL)
                .headers(basicAuthHeaders(testConfig.getAdminName(), ""))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void createUser_BaseAuth_Admin_Ok() {
        webTestClient
                .post()
                .uri(USER_URL)
                .body(BodyInserters.fromObject(getUser()))
                .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void createUser_BaseAuth_User_Forbidden() {
        webTestClient
                .post()
                .uri(USER_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(getUser()))
                .headers(basicAuthHeaders(testConfig.getUserName(), testConfig.getUserPassword()))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void greetAdmin_TokenAuth__Admin_Ok() {
        webTestClient
                .get()
                .uri(GREET_ADMIN_URL)
                .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), testConfig.getAdminPassword()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void greetAdmin_TokenAuth_User_Forbidden() {
        webTestClient
                .get()
                .uri(GREET_ADMIN_URL)
                .headers(tokenAuthHeaders(webTestClient, testConfig.getUserName(), testConfig.getUserPassword()))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void getAllUsers_TokenAuth_Admin_Ok() {
        webTestClient
                .get()
                .uri(USERS_URL)
                .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), testConfig.getAdminPassword()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void getAllUsers_TokenAuth_Anonymous_Unauthorized() {
        webTestClient
                .get()
                .uri(USERS_URL)
                .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), ""))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void createUser_TokenAuth_Admin_Ok() {
        webTestClient
                .post()
                .uri(USER_URL)
                .body(BodyInserters.fromObject(getUser()))
                .headers(tokenAuthHeaders(webTestClient, testConfig.getAdminName(), testConfig.getAdminPassword()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void createUser_TokenAuth_User_Forbidden() {
        webTestClient
                .post()
                .uri(USER_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(getUser()))
                .headers(tokenAuthHeaders(webTestClient, testConfig.getUserName(), testConfig.getUserPassword()))
                .exchange()
                .expectStatus().isForbidden();
    }

    private User getUser() {
        return User.builder().name(RandomStringUtils.random(10)).build();
    }
}
