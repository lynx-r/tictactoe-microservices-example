package com.tictactoe.authmodule.config;

import com.tictactoe.authmodule.auth.JWTAuthSuccessHandler;
import com.tictactoe.authmodule.auth.JWTAuthWebFilter;
import com.tictactoe.authmodule.service.JWTService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
//@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@ComponentScan("com.tictactoe.authmodule")
public class SpringSecurityWebFluxConfig {

  private static final String[] WHITELISTED_AUTH_URLS = {
      "/login",
      "/",
      "/public/**",
      "/auth/**"
  };

  /**
   * The test defined in SampleApplicationTests class will only get executed
   * if you change the authentication mechanism to basic (from form mechanism)
   * in SpringSecurityWebFluxConfig file
   *
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JWTService jwtService) {

    AuthenticationWebFilter authenticationJWT = new AuthenticationWebFilter(new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsRepository()));
    authenticationJWT.setAuthenticationSuccessHandler(new JWTAuthSuccessHandler(jwtService));

    http.csrf().disable();

    http
        .authorizeExchange()
        .pathMatchers(WHITELISTED_AUTH_URLS)
        .permitAll()
        .and()
        .addFilterAt(authenticationJWT, SecurityWebFiltersOrder.FIRST)
        .exceptionHandling()
        .and()
        .authorizeExchange()
        .pathMatchers("/actuator/**").hasRole("ADMIN")
        .pathMatchers(HttpMethod.GET, "/url-protected/games/**").hasRole("USER")
        .pathMatchers(HttpMethod.POST, "/url-protected/game/**").hasRole("ADMIN")
        .pathMatchers(HttpMethod.GET, "/v1/users/**").hasRole("USER")
        .pathMatchers(HttpMethod.POST, "/v1/users/**").hasRole("ADMIN")
        .pathMatchers(HttpMethod.GET, "/v1/games/**").hasRole("USER")
        .pathMatchers(HttpMethod.POST, "/v1/games/**").hasRole("ADMIN")
        .anyExchange()
        .authenticated()
        .and()
        .addFilterAt(new JWTAuthWebFilter(jwtService), SecurityWebFiltersOrder.HTTP_BASIC)
        .exceptionHandling();

    return http.build();
  }

  @Bean
  public MapReactiveUserDetailsService userDetailsRepository() {
    UserDetails anonymous = User.withUsername("anonymous").password("{noop}secret").roles("GUEST").build();
    UserDetails user = User.withUsername("user").password("{noop}password").roles("USER").build();
    UserDetails admin = User.withUsername("admin").password("{noop}password").roles("USER", "ADMIN").build();
    return new MapReactiveUserDetailsService(anonymous, user, admin);
  }

}
