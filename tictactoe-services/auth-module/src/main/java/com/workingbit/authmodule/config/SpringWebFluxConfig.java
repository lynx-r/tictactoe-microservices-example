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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tictactoe.domain.config.DomainModuleConfig;
import com.tictactoe.domain.repo.UserRepository;
import com.workingbit.authmodule.auth.JwtService;
import org.bson.types.ObjectId;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Configuration
@ComponentScan({"com.workingbit.authmodule"})
//@EnableReactiveMongoRepositories("com.tictactoe.domain.repo")
@Import(DomainModuleConfig.class)
public class SpringWebFluxConfig {

  private final ApplicationClientsProperties applicationClients;
  private final LoadBalancerExchangeFilterFunction lbFunction;

  public SpringWebFluxConfig(ApplicationClientsProperties applicationClients,
                             LoadBalancerExchangeFilterFunction lbFunction) {
    this.applicationClients = applicationClients;
    this.lbFunction = lbFunction;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  @Primary
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
  public ReactiveUserDetailsService userDetailsRepository(UserRepository users) {
    return (email) -> users.findByEmail(email).cast(UserDetails.class);
  }

  @Bean
  public WebClient loadBalancedWebClientBuilder(JwtService jwtService) {
    return WebClient.builder()
            .filter(lbFunction)
            .filter(authorizationFilter(jwtService))
            .build();
  }

  @Bean
  @Primary
  ObjectMapper objectMapper() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    builder.serializerByType(ObjectId.class, new ToStringSerializer());
    builder.deserializerByType(ObjectId.class, new JsonDeserializer() {
      @Override
      public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Map oid = p.readValueAs(Map.class);
        return new ObjectId(
                (Integer) oid.get("timestamp"),
                (Integer) oid.get("machineIdentifier"),
                ((Integer) oid.get("processIdentifier")).shortValue(),
                (Integer) oid.get("counter"));
      }
    });
    builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return builder.build();
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
