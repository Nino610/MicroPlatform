package com.elnino.security.mapper;

import com.elnino.security.domain.User;
import com.elnino.security.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public  User dtoConvert(UserDto dto)
    {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setSex(dto.getSex());
        user.setAge(dto.getAge());
        user.setAddress(dto.getAddress());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }
}
