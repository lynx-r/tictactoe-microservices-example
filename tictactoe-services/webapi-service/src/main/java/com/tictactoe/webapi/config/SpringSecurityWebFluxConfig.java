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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    private static final String[] WHITELISTED_AUTH_URLS = {
            "/",
            "/auth/**",
            "/method-protected/**",
            "/public/**",
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
    public SecurityWebFilterChain systemSecurityFilterChain(
            ServerHttpSecurity http, JwtService jwtService,
            @Qualifier("userDetailsRepositoryInMemory") ReactiveUserDetailsService userDetailsServiceInMemory,
            @Qualifier("userDetailsRepository") ReactiveUserDetailsService userDetailsService
    ) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManagerInMemory
                = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsServiceInMemory);

        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager
                = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);

        AuthenticationWebFilter tokenWebFilter = new AuthenticationWebFilter(authenticationManager);
        tokenWebFilter.setServerAuthenticationConverter(exchange ->
                Mono.justOrEmpty(exchange)
                        .filter(ex -> ex.getRequest().getPath().value().equalsIgnoreCase("/auth/token"))
                        .flatMap(ServerWebExchange::getFormData)
                        .filter(formData -> !formData.isEmpty())
                        .map((formData) -> {
                            String email = formData.getFirst("email");
                            String password = formData.getFirst("password");
                            return new UsernamePasswordAuthenticationToken(email, password);
                        })
        );
        tokenWebFilter.setAuthenticationSuccessHandler(new JwtAuthSuccessHandler(jwtService));

        WebApiServiceJwtAuthWebFilter webApiJwtServiceWebFilter = new WebApiServiceJwtAuthWebFilter(jwtService);
        http.csrf().disable();

        http
                .authorizeExchange()
                .pathMatchers(WHITELISTED_AUTH_URLS)
                .permitAll()
                .and()
                .authorizeExchange()
                .pathMatchers("/actuator/**").hasRole("SYSTEM")
                .pathMatchers(HttpMethod.GET, "/url-protected/**").hasRole("USER")
                .pathMatchers(HttpMethod.POST, "/url-protected/**").hasRole("ADMIN")
                .and()
                .httpBasic()
                .and()
                .authorizeExchange()
                .pathMatchers("/auth/token").authenticated()
                .and()
                .addFilterAt(webApiJwtServiceWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(tokenWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

}
