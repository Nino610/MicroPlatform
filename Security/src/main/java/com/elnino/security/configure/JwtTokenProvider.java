package com.elnino.security.configure;

import com.elnino.security.dto.Role;
import com.elnino.security.dto.UserDto;
import com.elnino.security.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
//    private final String JWT_SECRET = "elnino";
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Autowired
    UserRepository userRepository;

    public String generateToken(UserDto userDto) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
//        List<String> roleNames = userRepository.findRoleNamesByUsername(userDto.getName());
//        List<String> permissionNames = userRepository.findPermissionByUserName(userDto.getName());
//        userDto.setRoles(roleNames);
//        userDto.setPermissions(permissionNames);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("elnino")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("Info User", userDto)
                .claim("roles", userDto.getRoles())
                .claim("permissions", userDto.getPermissions())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject object = new JWSObject(header,payload);
        object.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return object.serialize();
    }
}