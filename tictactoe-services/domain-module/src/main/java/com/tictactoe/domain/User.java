package com.tictactoe.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 05:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("user")
public class User {

  @Id
  private String id;
  private String name;
  private Integer score;

  @JsonCreator
  public User(@JsonProperty("name") String name, @JsonProperty("score") Integer score) {
    this.name = name;
    this.score = score;
  }
}
