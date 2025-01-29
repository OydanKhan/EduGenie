package com.usyd.edugenie.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author caorong
 */

@Service
public class JwtTokenProvider {
    private final String SECRET = "EduGenie";
    private final String ISSUER = "EduGenie";

    private final long EXPIRATION_TIME = 3600 * 1000 * 8;

    public String generateJwtToken(String userEmail) {
        return JWT.create()
                .withSubject(userEmail)
                .withIssuer(ISSUER)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public DecodedJWT verifyJwtToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET))
                .withIssuer(ISSUER)
                .build();
        return verifier.verify(token);
    }

    public String getUserEmailFromDecoded(DecodedJWT decodedJwt) {
        return decodedJwt.getSubject();
    }

    public boolean isTokenExpired (DecodedJWT decodedJwt) {
        return decodedJwt.getExpiresAt().before(new Date());
    }

    public String refreshToken(String userEmail) {
        return generateJwtToken(userEmail);
    }
}
