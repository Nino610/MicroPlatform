package com.elnino.security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String sex;
    private Long age;
    private String address;
    private String userName;
    private String password;
    private String role;
}
