package com.nsteuerberg.gametracker.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nsteuerberg.gametracker.auth.config.SecurityProperties;
import com.nsteuerberg.gametracker.auth.persistance.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final String userGenerator;
    private final Integer expireInMinutes;
    private final Algorithm algorithm;

    public JwtService(SecurityProperties securityProperties,RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
        this.userGenerator = securityProperties.token().jwt().witUser();
        this.expireInMinutes = securityProperties.token().jwt().expiresInMinutes();
        algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
    }

    public String generateToken(UserEntity user) {
        List<String> authorities = List.of("ROLE_" + user.getRole().name());
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(userGenerator)
                .withSubject(user.getUserId().toString())
                .withClaim("authorities",authorities)
                .withIssuedAt(now)
                .withExpiresAt(now.plus(expireInMinutes, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        try {
            token = token.replace("Bearer ", "");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(userGenerator)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e){
            throw new JWTVerificationException(e.getMessage());
        }
    }

    public Long extractUserId(DecodedJWT decodedJWT) {
        return Long.parseLong(decodedJWT.getSubject());
    }

    public Set<GrantedAuthority> extractAuthorities(DecodedJWT decodedJWT) {
        Claim claim = decodedJWT.getClaim("authorities");
        return claim.asList(String.class).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
