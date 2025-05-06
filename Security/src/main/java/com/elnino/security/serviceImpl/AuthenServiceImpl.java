package com.elnino.security.serviceImpl;

import com.elnino.security.dto.AuthenticationRequest;
import com.elnino.security.dto.AuthenticationResponse;
import com.elnino.security.domain.User;
import com.elnino.security.service.AuthenService;
import com.elnino.security.configure.JwtTokenProvider;
import com.elnino.security.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenServiceImpl implements AuthenService {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private AuthenticationManager authenticationManager;

    @Override
    public String login(User user) throws JOSEException {
        // Xác thực người dùng
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                user.getName(), user.getPassword()
//        ));

        // Lưu thông tin Authentication vào SecurityContext
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Sinh token JWT từ JwtTokenProvider
        String token = jwtTokenProvider.generateToken(user);

        // In token ra console (có thể bỏ khi sản phẩm đi vào sử dụng)
        System.out.println(token);

        return token;
    }
    public AuthenticationResponse authentication(AuthenticationRequest request) throws JOSEException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userService.loadByUserName(request.getUsername());
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        var token = jwtTokenProvider.generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }
}
