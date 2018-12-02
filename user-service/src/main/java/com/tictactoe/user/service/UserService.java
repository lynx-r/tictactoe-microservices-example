package com.tictactoe.user.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tictactoe.domain.User;
import com.tictactoe.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 05:33
 */
@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @HystrixCommand
  public Flux<User> getAllUsers() {
    return userRepository.findAll();
  }

  @HystrixCommand
  public Mono<User> getUser(String userId) {
    return userRepository.findById(userId);
  }
}
