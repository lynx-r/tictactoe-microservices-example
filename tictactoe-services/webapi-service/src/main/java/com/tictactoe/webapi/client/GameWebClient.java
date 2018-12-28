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

import com.tictactoe.domainmodule.domain.Game;
import com.tictactoe.webapi.config.ApplicationConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 07:05
 */
@Service
public class GameWebClient {

    private final WebClient webClient;

    public GameWebClient(WebClient webClient,
                         ApplicationConfig applicationConfig) {
        this.webClient = webClient
                .mutate()
                .baseUrl(applicationConfig.getGameServiceUrl())
                .build();
    }

    public Flux<Game> getAllGames() {
        return webClient
                .get()
                .uri("/v1/games")
                .retrieve()
                .bodyToFlux(Game.class);
    }

    public Mono<Game> createGame(Map<String, Object> params) {
        Boolean black = (Boolean) params.remove("black");
        return webClient
                .post()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/v1/games/{userFirst}/{userSecond}")
                                .queryParam("black", black)
                                .build(params)
                )
                .retrieve()
                .bodyToMono(Game.class);
    }

    public Mono<Game> getGame(String gameId) {
        return webClient
                .get()
                .uri("/v1/games/{gameId}", gameId)
                .retrieve()
//        .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(new RuntimeException("4xx")))
//        .onStatus(HttpStatus::is5xxServerError, resp -> Mono.error(new RuntimeException("5xx")))
                .bodyToMono(Game.class);
    }
}
