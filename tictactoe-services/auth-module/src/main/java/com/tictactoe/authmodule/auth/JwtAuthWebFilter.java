package com.tictactoe.authmodule.auth;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public abstract class JwtAuthWebFilter implements WebFilter {

  private final ReactiveAuthenticationManager reactiveAuthManager = new JwtReactiveAuthManager();
  private Function<ServerWebExchange, Mono<Authentication>> jwtAuthConverter;
  private ServerAuthenticationSuccessHandler authSuccessHandler = new WebFilterChainServerAuthenticationSuccessHandler();
  private ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository.getInstance();

  public JwtAuthWebFilter(JwtService jwtService) {
    jwtAuthConverter = new JwtAuthConverter(jwtService);
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    return this.getAuthMatcher().matches(exchange)
        .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
        .flatMap(matchResult -> this.jwtAuthConverter.apply(exchange))
        .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
        .flatMap(token -> authenticate(exchange, chain, token));
  }

  protected abstract ServerWebExchangeMatcher getAuthMatcher();

  private Mono<Void> authenticate(ServerWebExchange exchange,
                                  WebFilterChain chain, Authentication token) {
    WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
    return reactiveAuthManager.authenticate(token)
        .flatMap(authentication -> onAuthSuccess(authentication, webFilterExchange));
  }

  private Mono<Void> onAuthSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
    ServerWebExchange exchange = webFilterExchange.getExchange();
    SecurityContextImpl securityContext = new SecurityContextImpl();
    securityContext.setAuthentication(authentication);
    return securityContextRepository.save(exchange, securityContext)
        .then(authSuccessHandler
            .onAuthenticationSuccess(webFilterExchange, authentication))
        .subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
  }
}
