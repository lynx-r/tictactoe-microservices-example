package com.tictactoecorp.user.config;

import com.tictactoecorp.domain.User;
import com.tictactoecorp.user.repository.UserRepository;
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
    userRepository.deleteAll()
        .subscribe();
    userRepository.save(new User("lala", 0))
        .subscribe();
    userRepository.save(new User("tata", 0))
        .subscribe();
  }
}