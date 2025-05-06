package com.elnino.security.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    @Override
    public Jwt decode(String token) throws JwtException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(),"HS512"); //dùng thuật toán HS512 để mã hóa - thay đổi phù hợp
        NimbusJwtDecoder jwtDecoder =  NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
        return jwtDecoder.decode(token);
    }
}