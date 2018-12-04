package com.tictactoe.domain;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthRequest implements Serializable {
  private String username;
  private String password;
}
