package com.tictactoe.authmodule.auth;

import com.tictactoe.authmodule.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JwtAuthSuccessHandler implements ServerAuthenticationSuccessHandler {

  private final JwtService jwtService;

  public JwtAuthSuccessHandler(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    ServerWebExchange exchange = webFilterExchange.getExchange();
    exchange.getResponse()
        .getHeaders()
        .add(HttpHeaders.AUTHORIZATION, jwtService.getHttpAuthHeaderValue(authentication));
    return webFilterExchange.getChain().filter(exchange);
  }
}
