package com.elnino.security.mapper;

import com.elnino.security.domain.User;
import com.elnino.security.dto.Role;
import com.elnino.security.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

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
//        user.setRoles(setToRole(dto.getRoles()));
        return user;
    }

    public UserDto convertEntity(User user){
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSex(user.getSex());
        dto.setAge(user.getAge());
        dto.setAddress(user.getAddress());
        dto.setPassword(user.getPassword());
//        dto.setRoles(convertRoleToSet(user.getRoles()));
        return dto;
    }
    private Role setToRole(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) return null;
        if (roles.contains(Role.ADMIN)) return Role.ADMIN;
        if (roles.contains(Role.USER)) return Role.USER;
        return roles.iterator().next();
    }
    public Set<Role> convertRoleToSet(Role role) {
        if (role == null) {
            return Collections.emptySet();
        }
        return Collections.singleton(role);
    }

}
