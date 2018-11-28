package com.tictactoecorp.gameservice.model;

import lombok.Data;
import org.bson.types.ObjectId;
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
  private ObjectId userBlack;

  @DBRef
  private ObjectId userWhite;

  private List<List<Boolean>> field;

  public Game(String userBlack, String userWhite) {
    this.userBlack = new ObjectId(userBlack);
    this.userWhite = new ObjectId(userWhite);
  }
}
