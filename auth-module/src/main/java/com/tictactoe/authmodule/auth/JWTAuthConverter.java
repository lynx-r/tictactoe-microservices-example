package com.tictactoe.authmodule.auth;

import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class JWTAuthConverter implements Function<ServerWebExchange, Mono<Authentication>> {
  @Override
  public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {
    return Mono.justOrEmpty(serverWebExchange)
        .map(JWTUtil::getAuthorizationPayload)
        .filter(JWTUtil.matchBearerLength())
        .map(JWTUtil.getBearerValue())
        .filter(token -> !token.isEmpty())
        .map(JWTUtil::verifySignedJWT)
        .flatMap(JWTUtil::getUsernamePasswordAuthenticationToken);
  }
}
