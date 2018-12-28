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

import com.workingbit.authmodule.auth.JwtAuthWebFilter;
import com.workingbit.authmodule.auth.JwtService;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: aleksey
 * Date: 2018-12-03
 * Time: 09:29
 */
public class MicroserviceServiceJwtAuthWebFilter extends JwtAuthWebFilter {

    private final String[] matchersStrings;

    MicroserviceServiceJwtAuthWebFilter(JwtService jwtService, String[] matchersStrings) {
        super(jwtService);
        this.matchersStrings = matchersStrings;
    }

    @Override
    protected ServerWebExchangeMatcher getAuthMatcher() {
        List<ServerWebExchangeMatcher> matchers = Arrays.stream(this.matchersStrings)
                .map(PathPatternParserServerWebExchangeMatcher::new)
                .collect(Collectors.toList());
        return ServerWebExchangeMatchers.matchers(new OrServerWebExchangeMatcher(matchers));
    }
}
