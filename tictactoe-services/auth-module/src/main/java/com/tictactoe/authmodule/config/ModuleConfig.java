package com.tictactoe.authmodule.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 11:59
 */
@Data
@Configuration
@PropertySource("classpath:moduleConfig.yml")
public class ModuleConfig {

  @Value("${tokenExpirationMinutes:60}")
  private Integer tokenExpirationMinutes;

  @Value("${tokenIssuer:tictactoe-example.com}")
  private String tokenIssuer;

  @Value("${tokenSecret:secret}") // length minimum 256 bites
  private String tokenSecret;
}
