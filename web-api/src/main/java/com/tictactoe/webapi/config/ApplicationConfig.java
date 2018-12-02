package com.tictactoe.webapi.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 08:33
 */
@Data
@Configuration
public class ApplicationConfig {

  @Value("${userservice-url}")
  private String userServiceUrl;

  @Value("${gameservice-url}")
  private String gameServiceUrl;

}
