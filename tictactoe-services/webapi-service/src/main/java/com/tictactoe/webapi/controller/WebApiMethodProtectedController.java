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

import com.tictactoe.domainmodule.domain.User;
import com.tictactoe.webapi.service.WebApiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 08:49
 */
@RestController
@RequestMapping("method-protected")
public class WebApiMethodProtectedController {

    private final WebApiService webApiService;

    public WebApiMethodProtectedController(WebApiService webApiService) {
        this.webApiService = webApiService;
    }

    @GetMapping("greetAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Map> greetAdmin() {
        return Mono.just(Map.of("greet", "Hi admin"));
    }

    @GetMapping("users")
    public Flux<User> getAllUsers() {
        return webApiService.getAllUsers();
    }

    @PostMapping("user")
    public Mono<User> createUser(@RequestBody User userRequest) {
        return webApiService.createUser(userRequest);
    }

}
