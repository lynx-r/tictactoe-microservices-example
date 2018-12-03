//package com.tictactoe.authmodule.config;
//
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
///**
// * User: aleksey
// * Date: 2018-12-03
// * Time: 13:13
// */
//@Configuration
//@EnableConfigurationProperties(DefaultClients.class)
//public class SpringSecurityConfig {
//
//  private final DefaultClients application;
//
//  public SpringSecurityConfig(DefaultClients application) {
//    this.application = application;
//  }
//
//  @Bean
//  public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//    final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
////    log.info("Importing {} clients:", application.getClients().size());
//
//    application.getClients().forEach(client -> {
//      manager.createUser(User.withDefaultPasswordEncoder()
//          .username(client.getUsername())
//          .password(client.getPassword())
//          .roles(client.getRoles())
//          .build());
////      log.info("Imported client {}", client.toString());
//    });
//
//    return manager;
//  }
//}
