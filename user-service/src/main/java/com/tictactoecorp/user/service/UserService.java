package com.tictactoecorp.user.service;

import com.tictactoecorp.domain.User;
import com.tictactoecorp.user.repository.UserRepository;
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

  public Flux<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Mono<User> getUser(String userId) {
    return userRepository.findById(userId);
  }
}
