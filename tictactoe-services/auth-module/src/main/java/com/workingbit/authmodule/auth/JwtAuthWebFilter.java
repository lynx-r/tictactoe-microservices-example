


/*
 * © Copyright 2018 Aleksey Popryadukhin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.workingbit.authmodule.auth;

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
    return getAuthMatcher().matches(exchange)
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
