package com.bashkir777.authservice.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bashkir777.authservice.dto.TokenPair;
import com.bashkir777.authservice.services.enums.Role;
import com.bashkir777.authservice.services.enums.TokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final Algorithm algorithm;

    public JwtService(@Value("jwt.secret") String secret){
        this.algorithm = Algorithm.HMAC256(secret);
    }


    public String createJwt(String email, TokenType tokenType, Role role){
        return JWT.create()
                .withIssuer("auth-service")
                .withSubject(email)
                .withClaim("role", role.name())
                .withClaim("type", tokenType.name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenType.getTimeAliveMillis()))
                .sign(algorithm);
    }

    public TokenPair createTokenPair(String email, Role role){
        return TokenPair.builder()
                .accessToken(createJwt(email, TokenType.ACCESS, role))
                .refreshToken(createJwt(email, TokenType.REFRESH, role))
                .build();
    }
    public DecodedJWT decodeAndValidateToken(String jwt) throws JWTVerificationException {
        return JWT.require(algorithm)
                .withIssuer("auth-service")
                .build()
                .verify(jwt);
    }

    public String getEmailFromToken(String jwt) throws JWTVerificationException{
        return decodeAndValidateToken(jwt).getSubject();
    }

    public TokenType getTypeFromToken(String jwt) throws JWTVerificationException{
        return TokenType.valueOf(decodeAndValidateToken(jwt).getClaim("type").asString());
    }

    public Role getRoleFromToken(String jwt) throws JWTVerificationException{
        return Role.valueOf(decodeAndValidateToken(jwt).getClaim("role").asString());
    }

}
