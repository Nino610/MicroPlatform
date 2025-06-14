package com.elnino.security.configure;

import com.elnino.security.domain.User;
import com.elnino.security.dto.Role;
import com.elnino.security.dto.UserDto;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final String JWT_SECRET = "elnino";
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    public String generateToken(UserDto userDto) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        Set<Role> roles = userDto.getRoles();
        List<String> roleNames = roles.stream().map(Role::name).map(r -> "ROLE_" + r) // để Spring Security hiểu
                .collect(Collectors.toList());
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userDto.getName())
                .issuer("elnino")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("roles", roleNames)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject object = new JWSObject(header,payload);
        object.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return object.serialize();
    }
}