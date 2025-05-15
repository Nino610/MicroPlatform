package com.elnino.security.service;

import com.elnino.security.domain.User;
import com.elnino.security.dto.UserDto;
import org.springframework.stereotype.Service;

public interface UserService {
    User loadByUserName(String userName);
    User createUser(UserDto dto);
}
