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

package com.tictactoe.webapi.controller;

import com.tictactoe.domainmodule.domain.JwtAuthResponse;
import com.tictactoe.domainmodule.domain.User;
import com.tictactoe.domainmodule.repo.UserRepository;
import com.workingbit.authmodule.auth.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.tictactoe.webapi.config.ErrorMessages.ERROR_HEADER_NAME;
import static com.tictactoe.webapi.config.ErrorMessages.USERNAME_BUSY;
import static org.springframework.http.ResponseEntity.badRequest;

/**
 * It is just an example of AuthController. Apply you fantasy here 😉
 */
@RestController
@RequestMapping(path = "auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("guest")
    public Mono<ResponseEntity<JwtAuthResponse>> guest(@RequestBody Map<String, Object> fingerprintMap) {
        String fingerprint = (String) fingerprintMap.get("fingerprint");
        return userRepository.findByFingerprintAndGuestFalse(fingerprint)
                .map(guest ->
                        ResponseEntity.ok(getJwtAuthResponse(guest)))
                .switchIfEmpty(
                        userRepository.findByFingerprint(fingerprint)
                                .map(guest ->
                                        ResponseEntity.ok(getJwtAuthResponse(guest)))
                                .switchIfEmpty(
                                        userRepository.insert(createGuestUser(fingerprint))
                                                .map((guest) -> ResponseEntity.ok(getJwtAuthResponse(guest)))));
    }

    @PostMapping("token")
    @PreAuthorize("isAuthenticated()")
    public Mono<ResponseEntity<JwtAuthResponse>> token(Authentication authentication)
            throws AuthenticationException {
        return userRepository.findByEmail(authentication.getName())
                .filter(user -> !user.isGuest())
                .flatMap(this::loginInternal)
                .map(user -> ResponseEntity.ok(getJwtAuthResponse(user)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("register")
    @PreAuthorize("hasRole('GUEST')")
    public Mono<ResponseEntity<JwtAuthResponse>> register(@RequestBody User user, Authentication authentication) {
        return userRepository
                .existsByEmail(user.getEmail())
                .filter(exists -> !exists)
                .flatMap((e) -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    if (user.getAuthorities().isEmpty()) {
                        user.addAuthority("ROLE_USER");
                        user.addAuthority("ROLE_GUEST");
                    } else {
                        user.retainAuthorities(List.of("ROLE_USER", "ROLE_GUEST"));
                    }
                    user.setGuest(false);
                    user.loggedIn();
                    String fingerprint = authentication.getName();
                    user.setFingerprint(fingerprint);
                    return userRepository.insert(user)
                            .map((u) -> ResponseEntity.ok(getJwtAuthResponse(u)));
                })
                .defaultIfEmpty(badRequest().header(ERROR_HEADER_NAME, USERNAME_BUSY).build());
    }

    @PostMapping("logout")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Mono<ResponseEntity<JwtAuthResponse>> logout(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .flatMap(this::logoutInternal)
                .map(user -> ResponseEntity.ok(getJwtAuthResponse(user)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    private User createGuestUser(String fingerprint) {
        return User.builder()
                .fingerprint(fingerprint)
                .email(fingerprint)
                .password(passwordEncoder.encode(""))
                .authorities(List.of("ROLE_GUEST"))
                .guest(true)
                .build();
    }

    private Mono<User> loginInternal(User user) {
        user.loggedIn();
        return userRepository.save(user);
    }

    private Mono<User> logoutInternal(User user) {
        user.loggedOut();
        return userRepository.save(user)
                .flatMap((u) -> userRepository.findByFingerprintAndGuestTrue(u.getFingerprint()));
    }

    private JwtAuthResponse getJwtAuthResponse(UserDetails userDetails) {
        String token = jwtService.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
        return new JwtAuthResponse(token, userDetails.getUsername());
    }
}
