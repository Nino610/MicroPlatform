package com.elnino.security.configure;

import com.elnino.security.domain.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
@Component
public class JwtTokenProvider {
    private final String JWT_SECRET = "elnino";
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    public String generateToken(User user) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getName())
                .issuer("elnino")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject object = new JWSObject(header,payload);
        object.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return object.serialize();
    }
}
