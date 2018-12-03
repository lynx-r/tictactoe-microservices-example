package com.tictactoe.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * User: aleksey
 * Date: 28/11/2018
 * Time: 08:52
 */
@Data
@Document("game")
public class Game {

  public static final int FIELD_SIZE = 3;

  @Id
  private String id;

  @DBRef
  private User userBlack;

  @DBRef
  private User userWhite;

  private List<List<Boolean>> field;

  @JsonCreator
  public Game(@JsonProperty("userBlack") User userBlack, @JsonProperty("userWhite") User userWhite) {
    this.userBlack = userBlack;
    this.userWhite = userWhite;
  }
}
