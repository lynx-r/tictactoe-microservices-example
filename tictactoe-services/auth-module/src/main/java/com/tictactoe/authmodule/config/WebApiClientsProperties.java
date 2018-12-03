package com.tictactoe.authmodule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: aleksey
 * Date: 2018-12-03
 * Time: 13:17
 */
@Data
@Component
@ConfigurationProperties("appclients")
public class WebApiClientsProperties {
  private List<ApplicationClient> clients = new ArrayList<>();

  @Data
  public static class ApplicationClient {
    private String username;
    private String password;
    private String[] roles;
  }

}

