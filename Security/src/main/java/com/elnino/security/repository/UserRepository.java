package com.elnino.security.repository;

import com.elnino.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByName(String name);

    Boolean existsUserByName(String name);
}
