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

package com.tictactoe.webapi.util;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;
import java.util.function.Consumer;

import static com.tictactoe.webapi.config.TestConstants.AUTH_TOKEN_URL;

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
                    .uri(AUTH_TOKEN_URL)
                    .headers(basicAuthHeaders(username, password))
                    .exchange()
                    .returnResult(Map.class)
                    .getResponseBody()
                    .blockFirst();
            if (token != null) {
                headers.setBearerAuth((String) token.get("token"));
            }
        };
    }

    public static Consumer<HttpHeaders> basicAuthHeaders(String username, String password) {
        return (headers) -> {
            headers.setBasicAuth(username, password);
        };
    }
}
