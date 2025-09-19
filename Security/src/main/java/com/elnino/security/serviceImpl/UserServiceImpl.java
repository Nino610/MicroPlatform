package com.elnino.security.serviceImpl;

import com.elnino.security.domain.User;
import com.elnino.security.dto.Role;
import com.elnino.security.dto.UserDto;
import com.elnino.security.mapper.UserMapper;
import com.elnino.security.repository.UserRepository;
import com.elnino.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User loadByUserName(String userName) {
        User user = userRepository.findUserByName(userName);
        if(user == null)
        {
            throw new UsernameNotFoundException(userName);
        }
        return user;
    }
    public User createUser(UserDto dto){
        if(userRepository.existsUserByName(dto.getName()))
            throw new ApplicationContextException("Đã có user tồn tại");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        HashSet<Role> roles = new HashSet<>();
//        dto.getRoles().forEach(role -> {
//            roles.add(role);
//        });
//        dto.setRole(Role.USER.name());
        return userRepository.save(userMapper.dtoConvert(dto));
    }
    @Override
    public List<UserDto> loadAllUser(){
        List<UserDto> lstUserDto = userRepository.findAll().stream().map(userMapper::convertEntity).toList();
        return lstUserDto;
    }
    @Override
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
