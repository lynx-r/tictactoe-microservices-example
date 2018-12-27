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

package com.tictactoe.userservice.config;

import com.tictactoe.authmodule.auth.JwtAuthSuccessHandler;
import com.tictactoe.authmodule.auth.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableReactiveMethodSecurity
public class SpringSecurityWebFluxConfig {

    private static final String[] WHITELISTED_AUTH_URLS = {
            "/",
            "/public/**",
    };

    private final ReactiveUserDetailsService userDetailsRepositoryInMemory;
    private final ReactiveAuthenticationManager reactiveAuthManager;

    public SpringSecurityWebFluxConfig(
            @Qualifier("userDetailsRepositoryInMemory") ReactiveUserDetailsService userDetailsRepositoryInMemory
    ) {
        this.userDetailsRepositoryInMemory = userDetailsRepositoryInMemory;
        reactiveAuthManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsRepositoryInMemory);
    }

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
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JwtService jwtService) {

        AuthenticationWebFilter authenticationJWT = new AuthenticationWebFilter(new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsRepositoryInMemory));
        authenticationJWT.setAuthenticationSuccessHandler(new JwtAuthSuccessHandler(jwtService));

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
                .pathMatchers("/actuator/**").hasRole("SYSTEM")
                .pathMatchers(HttpMethod.GET, "/v1/users/**").hasRole("USER")
                .pathMatchers(HttpMethod.POST, "/v1/users/**").hasRole("ADMIN")
                .anyExchange()
                .authenticated()
                .and()
                .addFilterAt(new WebApiJwtAuthWebFilter(reactiveAuthManager, jwtService), SecurityWebFiltersOrder.HTTP_BASIC)
                .exceptionHandling();

        return http.build();
    }

}
