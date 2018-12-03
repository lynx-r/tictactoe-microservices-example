package com.tictactoe.authmodule.auth;

import com.tictactoe.authmodule.service.JWTService;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class JWTAuthConverter implements Function<ServerWebExchange, Mono<Authentication>> {

  private final JWTService jwtService;

  public JWTAuthConverter(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {
    return Mono.justOrEmpty(serverWebExchange)
        .map(jwtService::getAuthorizationPayload)
        .filter(jwtService.matchBearerLength())
        .map(jwtService.getBearerValue())
        .filter(token -> !token.isEmpty())
        .map(jwtService::verifySignedJWT)
        .flatMap(jwtService::getUsernamePasswordAuthenticationToken);
  }
}
