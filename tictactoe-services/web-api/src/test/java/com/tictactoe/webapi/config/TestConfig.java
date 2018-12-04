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
  @Value("${adminName}")
  private String adminName;

  @Value("${adminPassword}")
  private String adminPassword;

  @Value("${userName}")
  private String userName;

  @Value("${userPassword}")
  private String userPassword;

  public String getAdminName() {
    return adminName;
  }

  public void setAdminName(String adminName) {
    this.adminName = adminName;
  }

  public String getAdminPassword() {
    return adminPassword;
  }

  public void setAdminPassword(String adminPassword) {
    this.adminPassword = adminPassword;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }
}
