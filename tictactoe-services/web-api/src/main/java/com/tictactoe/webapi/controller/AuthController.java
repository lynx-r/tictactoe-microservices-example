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

import com.tictactoe.authmodule.auth.JwtService;
import com.tictactoe.domain.JwtAuthResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping(path = "auth", produces = {APPLICATION_JSON_UTF8_VALUE})
public class AuthController {

    private final MapReactiveUserDetailsService userDetailsRepository;
    private final JwtService jwtService;

    public AuthController(@Qualifier("userDetailsRepositoryInMemory") MapReactiveUserDetailsService userDetailsRepository, JwtService jwtService) {
        this.userDetailsRepository = userDetailsRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("token")
    @CrossOrigin("*")
    public Mono<ResponseEntity<JwtAuthResponse>> token(Authentication authentication)
            throws AuthenticationException {
        return userDetailsRepository.findByUsername(authentication.getName())
                .map(user -> {
                    String token = jwtService.getTokenFromAuthentication(authentication);
                    JwtAuthResponse authResponse = new JwtAuthResponse(token, user.getUsername());
                    return ResponseEntity.ok(authResponse);
                })
                .defaultIfEmpty(notFound().build());
    }
}
