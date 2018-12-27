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

package com.tictactoe.gameservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.tictactoe.domain.Game;
import com.tictactoe.domain.User;
import com.tictactoe.gameservice.client.UserWebClient;
import com.tictactoe.gameservice.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: aleksey
 * Date: 28/11/2018
 * Time: 08:51
 */
@Service
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final UserWebClient userClient;

    public GameService(GameRepository gameRepository,
                       UserWebClient userWebClient) {
        this.gameRepository = gameRepository;
        this.userClient = userWebClient;
    }

    @HystrixCommand
    public Flux<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @HystrixCommand(fallbackMethod = "buildFallbackAllGames",
            threadPoolKey = "licenseByOrgThreadPool",
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize", value = "30"),
                            @HystrixProperty(name = "maxQueueSize", value = "10")},
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")}
    )
    public Flux<Game> getAllGamesLong() {
//    logger.debug("LicenseService.getLicensesByOrg  Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        randomlyRunLong();
        return gameRepository.findAll();
    }

    public Mono<Game> createGame(String userBlackId, String userWhiteId, Boolean black) {
        Mono<User> userBlackMono = userClient.getUser(userBlackId);
        Mono<User> userWhiteMono = userClient.getUser(userWhiteId);
        return userBlackMono
                .zipWith(userWhiteMono, (userBlack, userWhite) -> {
                    Game game;
                    if (black) {
                        game = new Game(userBlack, userWhite);
                    } else {
                        game = new Game(userWhite, userBlack);
                    }
                    initGameField(game);
                    return game;
                })
                .flatMap(gameRepository::insert);
    }

    private void initGameField(Game game) {
        game.setField(new ArrayList<>(Game.FIELD_SIZE));
        for (int i = 0; i < Game.FIELD_SIZE; i++) {
            var row = new ArrayList<Boolean>(Game.FIELD_SIZE);
            for (int j = 0; j < Game.FIELD_SIZE; j++) {
                row.add(null);
            }
            game.getField().add(row);
        }
    }

    private void randomlyRunLong() {
        Random rand = new Random();

        int randomNum = rand.nextInt((3 - 1) + 1) + 1;

        if (randomNum == 3) sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Flux<Game> buildFallbackAllGames() {
        User fakeUserBlack = new User("fakeUserBlack", 0);
        User fakeUserWhite = new User("fakeUserWhite", 0);
        Game game = new Game(fakeUserBlack, fakeUserWhite);
        List<Game> games = List.of(game);
        return Flux.fromIterable(games);
    }
}
