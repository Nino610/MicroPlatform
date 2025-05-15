package com.elnino.security.controller;

import com.elnino.security.domain.User;
import com.elnino.security.dto.AuthenticationRequest;
import com.elnino.security.dto.AuthenticationResponse;
import com.elnino.security.dto.UserDto;
import com.elnino.security.service.AuthenService;
import com.elnino.security.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}