package com.tictactoe.authmodule.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 11:59
 */
@Data
@Configuration
@EnableConfigurationProperties
@PropertySource("classpath:module.yml")
public class ModuleConfig {

  @Value("${tokenIssuer:tictactoe-example.com}")
  private String tokenIssuer;

  @Value("${tokenSecret:secret}")
  private String tokenSecret;
}
