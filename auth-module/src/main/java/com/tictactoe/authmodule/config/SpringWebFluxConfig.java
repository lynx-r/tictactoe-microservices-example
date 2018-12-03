package com.tictactoe.authmodule.config;

import com.tictactoe.authmodule.service.JWTService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan("com.tictactoe.authmodule")
public class SpringWebFluxConfig {

  @Bean
  @LoadBalanced
  public WebClient.Builder loadBalancedWebClientBuilder(JWTService jwtService) {
    return WebClient.builder()
        .filter(authorizationFilter(jwtService));
  }

  private ExchangeFilterFunction authorizationFilter(JWTService jwtService) {
    return ExchangeFilterFunction
        .ofRequestProcessor(clientRequest ->
            ReactiveSecurityContextHolder.getContext()
                .map(securityContext ->
                    ClientRequest.from(clientRequest)
                        .header(HttpHeaders.AUTHORIZATION,
                            jwtService.getHttpAuthHeaderValue(securityContext.getAuthentication()))
                        .build()));
  }

}
