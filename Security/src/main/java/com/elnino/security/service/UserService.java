package com.elnino.security.service;

import com.elnino.security.domain.User;
import com.elnino.security.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    User loadByUserName(String userName);
    User createUser(UserDto dto);
    List<UserDto> loadAllUser();
    void deleteUser(Long id);
}
