
package com.tictactoecorp.user.config;

import com.tictactoecorp.authmodule.config.SpringSecurityWebFluxConfig;
import com.tictactoecorp.authmodule.config.SpringWebFluxConfig;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 07:04
 */
@Data
@Configuration
@Import({SpringSecurityWebFluxConfig.class, SpringWebFluxConfig.class})
public class ApplicationConfig {

}
