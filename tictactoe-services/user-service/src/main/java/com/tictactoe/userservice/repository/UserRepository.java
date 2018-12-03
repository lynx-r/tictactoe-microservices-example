package com.tictactoe.userservice.repository;

import com.tictactoe.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 05:31
 */
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
