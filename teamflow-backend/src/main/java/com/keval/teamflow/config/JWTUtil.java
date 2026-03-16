package com.keval.teamflow.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.keval.teamflow.domain.enums.UserRole;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    @Value("${JWT_SECRET}")
    private String secret;


    public String generateJWTToken(String email, String name,Long userId, List<String> role) throws IllegalArgumentException , JWTCreationException{
        long expirationTimeInMillis = System.currentTimeMillis() + (120 * 60 * 1000); // 120 minutes in milliseconds
        Date expirationDate = new Date(expirationTimeInMillis);
        return JWT.create()
                .withSubject(email)
                .withIssuer("teamflow")
                .withClaim("name", name)
                .withClaim("role", role)
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }
    public String validateTokenAndExtractSubject(String token) throws JWTVerificationException{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("teamflow")
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }
}
