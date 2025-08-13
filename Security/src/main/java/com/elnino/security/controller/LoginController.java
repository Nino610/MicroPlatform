package com.elnino.security.controller;

import com.elnino.security.domain.User;
import com.elnino.security.dto.AuthenticationRequest;
import com.elnino.security.dto.AuthenticationResponse;
import com.elnino.security.dto.UserDto;
import com.elnino.security.service.AuthenService;
import com.elnino.security.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class LoginController {
    AuthenService authenService;
    UserService userService;
    @PostMapping("/token")
    public ResponseEntity<AuthenticationResponse> getToken(@RequestBody AuthenticationRequest user) throws JOSEException {
        var rs = authenService.authentication(user);
        return ResponseEntity.ok(rs);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> hello(@RequestBody AuthenticationRequest user) throws JOSEException, ParseException {
        var rs = authenService.validateToken(user);
        return ResponseEntity.ok(rs);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserDto dto){
        User user = userService.createUser(dto);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("test")
    public String test(){
        return "hello world";
    }

    @GetMapping("/profile")
    @PreAuthorize("@securityService.checkUserPermission(authentication, #userId)")
    public String userProfile(@RequestParam String userId) {
        return "Profile: " + userId;
    }


    // Sample để cache dữ liệu (cần cấu hình thêm dùng tool nào, phải search gpt)
//    @Cacheable(value = "userRoles", key = "#username")
//    public User getUserWithRoles(String username) {
//        return userRepository.findByUsernameWithRoles(username);
//    }
}