package com.tictactoe.webapi.config;

import com.tictactoe.authmodule.auth.JWTAuthWebFilter;
import com.tictactoe.authmodule.service.JWTService;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.util.ArrayList;
import java.util.List;

/**
 * User: aleksey
 * Date: 2018-12-03
 * Time: 09:29
 */
public class WebApiJWTAuthWebFilter extends JWTAuthWebFilter {

  public WebApiJWTAuthWebFilter(JWTService jwtService) {
    super(jwtService);
  }

  @Override
  protected ServerWebExchangeMatcher getAuthMatcher() {
    List<ServerWebExchangeMatcher> matchers = new ArrayList<>();
    matchers.add(new PathPatternParserServerWebExchangeMatcher("/method-protected/**"));
    return ServerWebExchangeMatchers.matchers(new OrServerWebExchangeMatcher(matchers));
  }
}
