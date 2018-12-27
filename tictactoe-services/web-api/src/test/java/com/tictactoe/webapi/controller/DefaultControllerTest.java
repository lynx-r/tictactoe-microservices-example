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
    private TestConfig testConfig;
    @Autowired
    private DefaultController defaultController;
    private WebTestClient webTestClient;

    @Before
    public void setUp() throws Exception {
        webTestClient = WebTestClient.bindToController(defaultController).build();
    }

    @Test
    public void guestHome() {
        webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"greet\":\"Welcome Guest\"}");
    }

    @Test
    public void login() {
        webTestClient
                .get()
                .uri("/login")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"greet\":\"Welcome Guest, its Basic Authentication\"}");
    }

    @Test
    public void loginSuccess() {
        webTestClient
                .get()
                .uri("/login")
                .headers(basicAuthHeaders(testConfig.getAdminName(), testConfig.getAdminPassword()))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"greet\":\"Welcome Guest, its Basic Authentication\"}");
    }

    @Test
    public void loginFail() {
        webTestClient
                .get()
                .uri("/login")
                .headers(basicAuthHeaders(testConfig.getAdminName(), ""))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"greet\":\"Welcome Guest, its Basic Authentication\"}");
    }
}
