package com.tictactoe.authmodule.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tictactoe.authmodule.config.ModuleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@Service
public class JwtService {

  private static final String AUTHORITIES_CLAIM = "auths";

  private static final String BEARER = "Bearer ";
  private static final Date TOKEN_EXPIRATION_TIME = new Date(new Date().getTime() + 30 * 1000);
  private final Logger logger = LoggerFactory.getLogger(JwtService.class);
  private final ModuleConfig moduleConfig;

  public JwtService(ModuleConfig moduleConfig) {
    this.moduleConfig = moduleConfig;
  }

  public String getHttpAuthHeaderValue(Authentication authentication) {
    return String.join(" ", "Bearer", tokenFromAuthentication(authentication));
  }

  private String tokenFromAuthentication(Authentication authentication) {
    return generateToken(
        authentication.getName(),
        authentication.getAuthorities());
  }


  public String generateToken(String subjectName, Collection<? extends GrantedAuthority> authorities) {
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(subjectName)
        .issuer(moduleConfig.getTokenIssuer())
        .expirationTime(TOKEN_EXPIRATION_TIME)
        .claim(AUTHORITIES_CLAIM,
            authorities
                .parallelStream()
                .map(auth -> (GrantedAuthority) auth)
                .map(GrantedAuthority::getAuthority)
                .collect(joining(",")))
        .build();

    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

    try {
      signedJWT.sign(new MACSigner(moduleConfig.getTokenSecret()));
    } catch (JOSEException e) {
      logger.error("ERROR while signing JWT", e);
      return null;
    }

    return signedJWT.serialize();
  }

  public String getAuthorizationPayload(ServerWebExchange serverWebExchange) {
    String token = serverWebExchange.getRequest()
        .getHeaders()
        .getFirst(HttpHeaders.AUTHORIZATION);
    return token == null ? "" : token;
  }

  public Predicate<String> matchBearerLength() {
    return authValue -> authValue.length() > BEARER.length();
  }

  public Function<String, String> getBearerValue() {
    return authValue -> authValue.substring(BEARER.length());
  }

  public Mono<SignedJWT> verifySignedJWT(String token) {
    try {
      return Mono.just(SignedJWT.parse(token));
    } catch (ParseException e) {
      logger.error("ERROR while verify JWT", e);
      return Mono.empty();
    }
  }

  public Mono<Authentication> getUsernamePasswordAuthenticationToken(Mono<SignedJWT> signedJWTMono) {
    return signedJWTMono
        .map((signedJWT -> {
          try {
            String subject = signedJWT.getJWTClaimsSet().getSubject();
            String auths = (String) signedJWT.getJWTClaimsSet().getClaim(AUTHORITIES_CLAIM);
            List<GrantedAuthority> authorities = Stream.of(auths.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(subject, null, authorities);
          } catch (ParseException e) {
            logger.error("ERROR while parse JWT login user as anonymous", e);
            return getAnonymousAuthentication();
          }
        }));
  }

  private AnonymousAuthenticationToken getAnonymousAuthentication() {
    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("GUEST"));
    return new AnonymousAuthenticationToken(
        generateToken("anonymous", authorities),
        "anonymous", authorities);
  }
}
