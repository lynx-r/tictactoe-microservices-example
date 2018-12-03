package com.tictactoe.webapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 10:52
 */
@RestController
@RequestMapping("public")
public class WebApiPublicController {

  @GetMapping("greet")
  public Mono<Map> greet() {
    return Mono.just(Map.of("greet", "Hi everybody"));
  }

}
