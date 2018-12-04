package com.tictactoe.webapi.config;

import com.tictactoe.authmodule.config.SpringWebFluxConfig;
import com.tictactoe.authmodule.config.WebApiClientsProperties;
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
@Import({WebApiClientsProperties.class, SpringWebFluxConfig.class})
public class ApplicationConfig {

  @Value("${userserviceUrl}")
  private String userServiceUrl;

  @Value("${gameserviceUrl}")
  private String gameServiceUrl;

}
