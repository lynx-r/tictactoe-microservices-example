package com.tictactoe.webapi.util;

import org.springframework.http.HttpHeaders;

import java.util.function.Consumer;

/**
 * User: aleksey
 * Date: 2018-12-04
 * Time: 06:40
 */
public class TestUtils {
  public static Consumer<HttpHeaders> basicAuthHeaders(String username, String password) {
    return (headers) -> {
      headers.setBasicAuth(username, password);
    };
  }
}
