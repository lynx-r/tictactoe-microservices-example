package com.tictactoe.webapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

/**
 * User: aleksey
 * Date: 2018-12-04
 * Time: 07:39
 */
@Configuration
@PropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
public class TestConfig {
  @Value("${prop}")
  private String prop;

  public String getProp() {
    return prop;
  }

  public void setProp(String prop) {
    this.prop = prop;
  }
}
