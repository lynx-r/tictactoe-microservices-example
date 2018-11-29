package com.tictactoecorp.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 05:05
 */
@Data
@Document("user")
public class User {

  @Id
  private String id;
  private String name;
  private String score;
}
