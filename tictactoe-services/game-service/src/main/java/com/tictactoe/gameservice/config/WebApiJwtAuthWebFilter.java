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

package com.tictactoe.gameservice.config;

import com.workingbit.authmodule.auth.JwtAuthWebFilter;
import com.workingbit.authmodule.auth.JwtService;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.util.ArrayList;
import java.util.List;

/**
 * User: aleksey
 * Date: 2018-12-03
 * Time: 09:29
 */
public class WebApiJwtAuthWebFilter extends JwtAuthWebFilter {

    public WebApiJwtAuthWebFilter(JwtService jwtService) {
        super(jwtService);
    }

    @Override
    protected ServerWebExchangeMatcher getAuthMatcher() {
        List<ServerWebExchangeMatcher> matchers = new ArrayList<>();
        matchers.add(new PathPatternParserServerWebExchangeMatcher("/v1/games/**", HttpMethod.GET));
        matchers.add(new PathPatternParserServerWebExchangeMatcher("/v1/games/**", HttpMethod.POST));
        return ServerWebExchangeMatchers.matchers(new OrServerWebExchangeMatcher(matchers));
    }
}
