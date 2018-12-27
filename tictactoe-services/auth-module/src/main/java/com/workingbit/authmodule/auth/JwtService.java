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

package com.workingbit.authmodule.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.workingbit.authmodule.config.AuthModuleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

@Service
public class JwtService {

  private static final String AUTHORITIES_CLAIM = "auths";

  private static final String BEARER = "Bearer ";
  private static final JWSAlgorithm JWS_ALGORITHM = JWSAlgorithm.HS256;
  private static final String SECRET_KEY_ALGORITHM = "HMAC";
  private final Logger logger = LoggerFactory.getLogger(JwtService.class);
  private final AuthModuleConfig authModuleConfig;

  public JwtService(AuthModuleConfig authModuleConfig) {
    this.authModuleConfig = authModuleConfig;
  }

  public String getHttpAuthHeaderValue(Authentication authentication) {
    String token = getTokenFromAuthentication(authentication);
    return String.join(" ", "Bearer", token);
  }

  public String getTokenFromAuthentication(Authentication authentication) {
    return generateToken(
            authentication.getName(),
            authentication.getAuthorities());
  }

  public String generateToken(String subjectName, Collection<? extends GrantedAuthority> authorities) {
    Date expirationTime = Date.from(Instant.now().plus(authModuleConfig.getTokenExpirationMinutes(), ChronoUnit.MINUTES));
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(subjectName)
            .issuer(authModuleConfig.getTokenIssuer())
            .expirationTime(expirationTime)
            .claim(AUTHORITIES_CLAIM,
                    authorities
                            .parallelStream()
                            .map(auth -> (GrantedAuthority) auth)
                            .map(GrantedAuthority::getAuthority)
                            .collect(joining(",")))
            .build();

    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWS_ALGORITHM), claimsSet);

    try {
      final SecretKey key = new SecretKeySpec(authModuleConfig.getTokenSecret().getBytes(), SECRET_KEY_ALGORITHM);
      signedJWT.sign(new MACSigner(key));
    } catch (JOSEException e) {
      logger.error("ERROR while signing JWT", e);
      return null;
    }

    return signedJWT.serialize();
  }

  String getAuthorizationPayload(ServerWebExchange serverWebExchange) {
    String token = serverWebExchange.getRequest()
            .getHeaders()
            .getFirst(HttpHeaders.AUTHORIZATION);
    return token == null ? "" : token;
  }

  Predicate<String> matchBearerLength() {
    return authValue -> authValue.length() > BEARER.length();
  }

  Function<String, String> getBearerValue() {
    return authValue -> authValue.substring(BEARER.length());
  }

  Mono<JWTClaimsSet> verifySignedJWT(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(authModuleConfig.getTokenSecret());
      boolean valid = signedJWT.verify(verifier);
      if (valid) {
        ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector((header, context) -> {
          final SecretKey key = new SecretKeySpec(authModuleConfig.getTokenSecret().getBytes(), SECRET_KEY_ALGORITHM);
          return List.of(key);
        });
        JWTClaimsSet claimsSet = jwtProcessor.process(signedJWT, null);
        return Mono.just(claimsSet);
      } else {
        logger.error("ERROR TOKEN invalid " + token);
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid token"));
      }
    } catch (ParseException | JOSEException | BadJOSEException e) {
      logger.error("ERROR while verify JWT: " + token);
      return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "unable to verify token"));
    }
  }

  Mono<Authentication> getUsernamePasswordAuthenticationToken(Mono<JWTClaimsSet> claimsSetMono) {
    return claimsSetMono
            .map((claimsSet -> {
              String subject = claimsSet.getSubject();
              String auths = (String) claimsSet.getClaim(AUTHORITIES_CLAIM);
              List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(auths.split(","));
              return new UsernamePasswordAuthenticationToken(subject, null, authorities);
            }));
  }
}
