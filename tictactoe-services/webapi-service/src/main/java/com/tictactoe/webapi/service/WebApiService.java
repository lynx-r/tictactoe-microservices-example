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

package com.tictactoe.webapi.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tictactoe.domainmodule.domain.Game;
import com.tictactoe.domainmodule.domain.User;
import com.tictactoe.webapi.client.GameWebClient;
import com.tictactoe.webapi.client.UserWebClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 08:49
 */
@Service
public class WebApiService {

    private final UserWebClient userWebClient;
    private final GameWebClient gameWebClient;

    public WebApiService(
            UserWebClient userWebClient,
            GameWebClient gameWebClient
    ) {
        this.userWebClient = userWebClient;
        this.gameWebClient = gameWebClient;
    }

    @HystrixCommand
    @PreAuthorize("hasRole('GUEST')")
    public Flux<User> getAllUsers() {
        return userWebClient.getAllUsers();
    }

    @HystrixCommand
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<User> createUser(User userRequest) {
        return userWebClient.createUser(userRequest);
    }

    @HystrixCommand
    public Flux<Game> getAllGames() {
        return gameWebClient.getAllGames();
    }

    @HystrixCommand
    public Mono<Game> createGame(Map<String, Object> gameRequest) {
        return gameWebClient.createGame(gameRequest);
    }
}
