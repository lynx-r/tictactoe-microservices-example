package com.tictactoe.webapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApiApplicationTests {

  @Autowired
  private WebClient.Builder webClientBuilder;

  @Test
  public void testRoot() {
  }

}
