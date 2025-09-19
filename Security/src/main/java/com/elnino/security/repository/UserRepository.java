package com.elnino.security.repository;

import com.elnino.security.domain.Roles;
import com.elnino.security.domain.User;
import com.elnino.security.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByName(String name);

    Boolean existsUserByName(String name);

    @Query("SELECT r.type FROM User u JOIN u.roles r WHERE u.name = :username")
    List<String> findRoleNamesByUsername(@Param("username") String username);

}
