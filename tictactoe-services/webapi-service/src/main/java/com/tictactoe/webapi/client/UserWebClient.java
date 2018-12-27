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

package com.tictactoe.webapi.client;

import com.tictactoe.domain.User;
import com.tictactoe.webapi.config.ApplicationConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 07:05
 */
@Service
public class UserWebClient {

    private final WebClient.Builder webClientBuilder;
    private final String userServiceUrl;

    public UserWebClient(WebClient.Builder webClientBuilder,
                         ApplicationConfig applicationConfig) {
        this.webClientBuilder = webClientBuilder;
        this.userServiceUrl = applicationConfig.getUserServiceUrl();
    }

    public Flux<User> getAllUsers() {
        return webClientBuilder
                .build()
                .get()
                .uri(userServiceUrl + "/v1/users")
                .retrieve()
                .bodyToFlux(User.class);
    }

    public Mono<User> createUser(User userRequest) {
        return webClientBuilder
                .build()
                .post()
                .uri(userServiceUrl + "/v1/users")
                .body(BodyInserters.fromObject(userRequest))
                .retrieve()
                .bodyToMono(User.class);
    }

    public Mono<User> getUser(String userId) {
        return webClientBuilder
                .build()
                .get()
                .uri(userServiceUrl + "/v1/users/{userId}", userId)
                .retrieve()
                .bodyToMono(User.class);
    }
}
