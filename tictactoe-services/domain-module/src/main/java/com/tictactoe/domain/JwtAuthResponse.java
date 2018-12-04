package com.tictactoe.domain;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
  private String token;
  private String username;
}
