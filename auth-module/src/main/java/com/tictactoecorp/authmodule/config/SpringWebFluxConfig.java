package com.tictactoecorp.authmodule.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import static com.tictactoecorp.authmodule.auth.JWTAuthSuccessHandler.getHttpAuthHeaderValue;

@Configuration
public class SpringWebFluxConfig {

  private static ExchangeFilterFunction authorizationFilter() {
    return ExchangeFilterFunction
        .ofRequestProcessor(clientRequest ->
            ReactiveSecurityContextHolder.getContext()
                .map(securityContext ->
                    ClientRequest.from(clientRequest)
                        .header(HttpHeaders.AUTHORIZATION, getHttpAuthHeaderValue(securityContext.getAuthentication()))
                        .build()));
  }

  @Bean
  @LoadBalanced
  public WebClient.Builder loadBalancedWebClientBuilder() {
    return WebClient.builder()
        .filter(authorizationFilter());
  }

}
