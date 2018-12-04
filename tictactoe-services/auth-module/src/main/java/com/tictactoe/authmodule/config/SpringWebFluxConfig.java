package com.tictactoe.authmodule.config;

import com.tictactoe.authmodule.auth.JwtService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Configuration
@ComponentScan("com.tictactoe.authmodule")
public class SpringWebFluxConfig {

  private final WebApiClientsProperties applicationClients;

  public SpringWebFluxConfig(WebApiClientsProperties applicationClients) {
    this.applicationClients = applicationClients;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public MapReactiveUserDetailsService userDetailsRepositoryInMemory() {
    List<UserDetails> users = applicationClients.getClients()
        .stream()
        .map(applicationClient ->
            User.builder()
                .username(applicationClient.getUsername())
                .password(passwordEncoder().encode(applicationClient.getPassword()))
                .roles(applicationClient.getRoles()).build())
        .collect(toList());
    return new MapReactiveUserDetailsService(users);
  }

  @Bean
  @LoadBalanced
  public WebClient.Builder loadBalancedWebClientBuilder(JwtService jwtService) {
    return WebClient.builder()
        .filter(authorizationFilter(jwtService));
  }

  private ExchangeFilterFunction authorizationFilter(JwtService jwtService) {
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
