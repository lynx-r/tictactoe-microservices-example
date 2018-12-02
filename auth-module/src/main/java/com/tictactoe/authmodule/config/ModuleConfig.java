package com.tictactoe.authmodule.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 11:59
 */
@Data
@Configuration
public class ModuleConfig {

  @Value("${tokenIssuer:tictactoe-example.com}")
  private String tokenIssuer;
}
