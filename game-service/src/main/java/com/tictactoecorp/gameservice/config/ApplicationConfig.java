package com.tictactoecorp.gameservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 07:04
 */
@Data
@Configuration
public class ApplicationConfig {

  @Value("${userservice-url}")
  private String userServiceUrl;

}
