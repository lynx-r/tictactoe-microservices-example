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

package com.tictactoe.webapi.config;

import com.workingbit.authmodule.auth.JwtAuthSuccessHandler;
import com.workingbit.authmodule.auth.JwtService;
import com.workingbit.authmodule.config.MicroserviceServiceJwtAuthWebFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
public class SpringSecurityWebFluxConfig {

    private static final String AUTH_TOKEN_PATH = "/auth/token";

    @Value("${whiteListedAuthUrls}")
    private String[] whiteListedAuthUrls;
    @Value("${jwtTokenMatchUrls}")
    private String[] jwtTokenMatchUrls;

    @Bean
    @Primary
    public SecurityWebFilterChain systemSecurityFilterChain(
            ServerHttpSecurity http, JwtService jwtService,
            @Qualifier("userDetailsRepository") ReactiveUserDetailsService userDetailsService
    ) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager
                = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);

        AuthenticationWebFilter tokenWebFilter = new AuthenticationWebFilter(authenticationManager);
        tokenWebFilter.setServerAuthenticationConverter(exchange ->
                Mono.justOrEmpty(exchange)
                        .filter(ex -> AUTH_TOKEN_PATH.equalsIgnoreCase(ex.getRequest().getPath().value()))
                        .flatMap(ServerWebExchange::getFormData)
                        .filter(formData -> !formData.isEmpty())
                        .map((formData) -> {
                            String email = formData.getFirst("email");
                            String password = formData.getFirst("password");
                            return new UsernamePasswordAuthenticationToken(email, password);
                        })
        );
        tokenWebFilter.setAuthenticationSuccessHandler(new JwtAuthSuccessHandler(jwtService));

        MicroserviceServiceJwtAuthWebFilter webApiJwtServiceWebFilter = new MicroserviceServiceJwtAuthWebFilter(jwtService, jwtTokenMatchUrls);
        http.csrf().disable();

        http
                .authorizeExchange()
                .pathMatchers(whiteListedAuthUrls)
                .permitAll()
                .and()
                .authorizeExchange()
                .pathMatchers("/actuator/**").hasRole("SYSTEM")
                .pathMatchers(HttpMethod.GET, "/url-protected/**").hasRole("GUEST")
                .pathMatchers(HttpMethod.POST, "/url-protected/**").hasRole("USER")
                .and()
                .httpBasic()
                .and()
                .authorizeExchange()
                .pathMatchers(AUTH_TOKEN_PATH).authenticated()
                .and()
                .addFilterAt(webApiJwtServiceWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(tokenWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

}
