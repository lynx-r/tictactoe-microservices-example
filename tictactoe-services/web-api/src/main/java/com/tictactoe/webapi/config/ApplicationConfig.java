package com.tictactoe.webapi.config;

import com.tictactoe.authmodule.config.SpringSecurityWebFluxConfig;
import com.tictactoe.authmodule.config.SpringWebFluxConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 08:33
 */
@Data
@Configuration
@Import({SpringSecurityWebFluxConfig.class, SpringWebFluxConfig.class})
public class ApplicationConfig {

  @Value("${userservice-url}")
  private String userServiceUrl;

  @Value("${gameservice-url}")
  private String gameServiceUrl;

}
