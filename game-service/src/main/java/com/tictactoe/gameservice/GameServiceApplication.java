package com.tictactoe.gameservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class GameServiceApplication {

  private static final Logger log = LoggerFactory.getLogger(GameServiceApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(GameServiceApplication.class, args);
  }
}
