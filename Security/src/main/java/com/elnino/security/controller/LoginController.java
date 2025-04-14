package com.elnino.security.controller;

import com.elnino.security.domain.User;
import com.elnino.security.dto.AuthenticationRequest;
import com.elnino.security.dto.AuthenticationResponse;
import com.elnino.security.service.AuthenService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    AuthenService authenService;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> hello(@RequestBody AuthenticationRequest user) throws JOSEException {
        var rs = authenService.authentication(user);
        return ResponseEntity.ok(rs);
    }
}
