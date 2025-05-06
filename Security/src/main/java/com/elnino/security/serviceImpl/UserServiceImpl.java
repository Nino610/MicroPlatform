package com.elnino.security.serviceImpl;

import com.elnino.security.domain.User;
import com.elnino.security.repository.UserRepository;
import com.elnino.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public User loadByUserName(String userName) {
        User user = userRepository.findUserByName(userName);
        if(user == null)
        {
            throw new UsernameNotFoundException(userName);
        }
        return user;
    }
}
