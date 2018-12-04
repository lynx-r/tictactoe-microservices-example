package com.tictactoe.webapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class DefaultController {

  @GetMapping("/")
  public Mono<Map> guestHome() {
    return Mono.just(Map.of("greet", "Welcome Guest"));
  }

  @GetMapping("/login")
  public Mono<Map> login() {
    return Mono.just(Map.of("greet", "Welcome Guest, its Basic Authentication"));
  }
}
