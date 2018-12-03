package com.tictactoe.webapi;

import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 15:25
 */
public class MonoTest {

  @Test
  public void name() {
    Object aDefault = Mono.justOrEmpty("just")
        .filter(Objects::nonNull)
        .map((s) -> null)
        .filter(Objects::nonNull)
        .defaultIfEmpty("default")
        .block();
    System.out.println(aDefault);
  }
}
