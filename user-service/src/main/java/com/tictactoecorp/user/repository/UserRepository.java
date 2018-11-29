package com.tictactoecorp.user.repository;

import com.tictactoecorp.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 05:31
 */
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
