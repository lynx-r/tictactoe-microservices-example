package com.tictactoe.userservice.config;

import com.tictactoe.domain.User;
import com.tictactoe.userservice.repository.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

  private UserRepository userRepository;

  public DataLoader(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void run(ApplicationArguments args) {
//    userRepository.deleteAll()
//        .subscribe();
    userRepository.save(new User("lala-" + RandomStringUtils.randomAlphabetic(4), 0))
        .subscribe();
    userRepository.save(new User("tata-" + RandomStringUtils.randomAlphabetic(4), 0))
        .subscribe();
  }
}