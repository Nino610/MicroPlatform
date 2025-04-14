package com.elnino.security.service;

import com.elnino.security.domain.User;
import org.springframework.stereotype.Service;

public interface UserService {
    User loadByUserName(String userName);
}
