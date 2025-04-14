package com.elnino.security.dto;

import lombok.Getter;
import lombok.Setter;

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
}
