package com.elnino.security.controller;

import com.elnino.security.dto.UserDto;
import com.elnino.security.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @GetMapping("/loadAll")
    public List<UserDto>  loadAllUser(){
        return userService.loadAllUser();
    }

    @DeleteMapping("/deleteUser/{id}")
    public void deleteUser(@PathVariable("id") Long id){
         userService.deleteUser(id);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')") // Role từ JWT hoặc DB
//    @PreAuthorize("hasRole('ADMIN')") // Role từ JWT hoặc DB phải thêm tiền tố ROLE_
    public String adminOnly() {
        return "Admin content";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')") // Role từ JWT hoặc DB - role là USER hay ADMIN đều truy cập dược
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") //? Role từ JWT hoặc DB - role là USER hay ADMIN đều truy cập, dược phải thêm tiền tố ROLE_
    public String userOnly() {
        return "user content";
    }

    @GetMapping("/profile")
    @PreAuthorize("@userServiceImpl.checkUserPermission(authentication, #userId)")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public String userProfile(@RequestParam String userId) {
        return "Profile: " + userId;
    }
}
