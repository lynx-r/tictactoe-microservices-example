package com.tictactoe.authmodule.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JWTAuthSuccessHandler implements ServerAuthenticationSuccessHandler {

  public static String getHttpAuthHeaderValue(Authentication authentication) {
    return String.join(" ", "Bearer", tokenFromAuthentication(authentication));
  }

  private static String tokenFromAuthentication(Authentication authentication) {
    return JWTUtil.generateToken(
        authentication.getName(),
        authentication.getAuthorities());
  }

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    ServerWebExchange exchange = webFilterExchange.getExchange();
    exchange.getResponse()
        .getHeaders()
        .add(HttpHeaders.AUTHORIZATION, getHttpAuthHeaderValue(authentication));
    return webFilterExchange.getChain().filter(exchange);
  }
}
