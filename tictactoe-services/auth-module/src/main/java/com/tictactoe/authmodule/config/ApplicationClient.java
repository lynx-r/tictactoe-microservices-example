package com.tictactoe.authmodule.config;

import lombok.Data;

@Data
public class ApplicationClient {
  private String username;
  private String password;
  private String[] roles;
}
