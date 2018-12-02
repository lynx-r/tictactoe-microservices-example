package com.tictactoe.authmodule.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

public class JWTUtil {
  public final static String DEFAULT_SECRET = "packtpubpacktpubpacktpubpacktpub";
  private static final String BEARER = "Bearer ";
  public static final String TOKEN_ISSUER_COM = "tictactoe-example.com";
  public static final String CLAIM = "auths";

  public static String generateToken(String subjectName, Collection<? extends GrantedAuthority> authorities) {
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(subjectName)
        .issuer(TOKEN_ISSUER_COM)
        .expirationTime(new Date(new Date().getTime() + 30 * 1000))
        .claim(CLAIM,
            authorities
                .parallelStream()
                .map(auth -> (GrantedAuthority) auth)
                .map(GrantedAuthority::getAuthority)
                .collect(joining(",")))
        .build();

    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

    try {
      signedJWT.sign(JWTUtil.getJWTSigner());
    } catch (JOSEException e) {
      e.printStackTrace();
    }

    return signedJWT.serialize();
  }

  private static JWSSigner getJWTSigner() {
    JWSSigner jwsSigner;
    try {
      jwsSigner = new MACSigner(DEFAULT_SECRET);
    } catch (KeyLengthException e) {
      jwsSigner = null;
    }
    return jwsSigner;
  }

  static String getAuthorizationPayload(ServerWebExchange serverWebExchange) {
    return serverWebExchange.getRequest()
        .getHeaders()
        .getFirst(HttpHeaders.AUTHORIZATION);
  }

  static Predicate<String> matchBearerLength() {
    return authValue -> authValue.length() > BEARER.length();
  }

  static Function<String, String> getBearerValue() {
    return authValue -> authValue.substring(BEARER.length());
  }

  static Mono<SignedJWT> verifySignedJWT(String token) {
    try {
      return Mono.just(SignedJWT.parse(token));
    } catch (ParseException e) {
      return Mono.empty();
    }
  }

  static Authentication getUsernamePasswordAuthenticationToken(Mono<SignedJWT> signedJWTMono) {
    SignedJWT signedJWT = signedJWTMono.block();
    String subject;
    String auths;

    try {
      if (signedJWT != null) {
        subject = signedJWT.getJWTClaimsSet().getSubject();
        auths = (String) signedJWT.getJWTClaimsSet().getClaim("auths");
      } else {
        return null;
      }
    } catch (ParseException e) {
      return null;
    }
    List<GrantedAuthority> authorities = Stream.of(auths.split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    return new UsernamePasswordAuthenticationToken(subject, null, authorities);
  }
}
