package com.tictactoe.gameservice.config;

import com.tictactoe.authmodule.config.SpringWebFluxConfig;
import com.tictactoe.authmodule.config.WebApiClientsProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 07:04
 */
@Data
@Configuration
@Import({WebApiClientsProperties.class, SpringWebFluxConfig.class})
public class ApplicationConfig {

  @Value("${userserviceUrl}")
  private String userServiceUrl;

}
