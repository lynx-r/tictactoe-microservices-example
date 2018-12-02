package com.tictactoe.user.controller;

import com.tictactoe.domain.User;
import com.tictactoe.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 05:29
 */
@RestController
@RequestMapping("v1/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("")
  public Flux<User> getUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("{userId}")
  public Mono<User> getUser(@PathVariable String userId) {
    return userService.getUser(userId);
  }
}
