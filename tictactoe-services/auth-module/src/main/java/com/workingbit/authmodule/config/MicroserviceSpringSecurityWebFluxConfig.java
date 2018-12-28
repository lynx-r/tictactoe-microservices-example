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

package com.workingbit.authmodule.config;

import com.workingbit.authmodule.auth.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@ConditionalOnProperty(value = "microservice", havingValue = "true")
@EnableReactiveMethodSecurity
@PropertySource(value = "classpath:/application.properties")
public class MicroserviceSpringSecurityWebFluxConfig {

    @Value("${whiteListedAuthUrls}")
    private String[] whiteListedAuthUrls;
    @Value("${jwtTokenMatchUrls}")
    private String[] jwtTokenMatchUrls;

    /**
     * The test defined in SampleApplicationTests class will only get executed
     * if you change the authentication mechanism to basic (from form mechanism)
     * in SpringSecurityWebFluxConfig file
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http, JwtService jwtService
    ) {
        MicroserviceServiceJwtAuthWebFilter userServiceJwtAuthWebFilter
                = new MicroserviceServiceJwtAuthWebFilter(jwtService, jwtTokenMatchUrls);

        http.csrf().disable();

        http
                .authorizeExchange()
                .pathMatchers(whiteListedAuthUrls)
                .permitAll()
                .and()
                .authorizeExchange()
                .pathMatchers("/actuator/**").hasRole("SYSTEM")
                .and()
                .httpBasic()
                .and()
                .addFilterAt(userServiceJwtAuthWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

}
