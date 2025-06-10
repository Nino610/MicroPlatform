package com.elnino.security.serviceImpl;

import com.elnino.security.dto.AuthenticationRequest;
import com.elnino.security.dto.AuthenticationResponse;
import com.elnino.security.domain.User;
import com.elnino.security.service.AuthenService;
import com.elnino.security.configure.JwtTokenProvider;
import com.elnino.security.service.UserService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenServiceImpl implements AuthenService {
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private AuthenticationManager authenticationManager;
    public AuthenticationResponse authentication(AuthenticationRequest request) throws JOSEException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userService.loadByUserName(request.getUsername());
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated) throw new ApplicationContextException("UNANUTHENTICATED");
        var token = jwtTokenProvider.generateToken(user);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    public AuthenticationResponse validateToken(AuthenticationRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        signedJWT.verify(verifier);
        var verified = signedJWT.verify(verifier);
        Date exprityTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        return AuthenticationResponse.builder().authenticated(verified && exprityTime.after(new Date())).build();
    }

}