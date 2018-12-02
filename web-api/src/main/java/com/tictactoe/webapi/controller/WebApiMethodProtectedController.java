package com.tictactoe.webapi.controller;

import com.tictactoe.domain.User;
import com.tictactoe.webapi.service.WebApiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 08:49
 */
@RestController
@RequestMapping("method-protected")
public class WebApiMethodProtectedController {

  private final WebApiService webApiService;

  public WebApiMethodProtectedController(WebApiService webApiService) {
    this.webApiService = webApiService;
  }

  @GetMapping("users/greetAdmin")
  @PreAuthorize("hasRole('ADMIN')")
  public Mono<Map> greetAdmin() {
    return Mono.just(Map.of("greet", "Hi admin"));
  }

  @GetMapping("users")
  public Flux<User> getAllUsers() {
    return webApiService.getAllUsers();
  }

  @PostMapping("user")
  public Mono<User> createUser(@RequestBody User userRequest) {
    return webApiService.createUser(userRequest);
  }

}
