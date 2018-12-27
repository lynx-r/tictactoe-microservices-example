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
