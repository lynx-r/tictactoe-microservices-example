
package com.tictactoecorp.user.config;

import com.tictactoecorp.authmodule.config.SpringSecurityWebFluxConfig;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 07:04
 */
@Getter
@Configuration
@Import(SpringSecurityWebFluxConfig.class)
public class ApplicationConfig {

}
