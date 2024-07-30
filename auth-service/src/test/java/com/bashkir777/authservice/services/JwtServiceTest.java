package com.bashkir777.authservice.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bashkir777.authservice.services.enums.Role;
import com.bashkir777.authservice.services.enums.TokenType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("Jwt service correctly encodes end decodes token types")
    public void JwtServiceCorrectlyEncodesEndDecodesTokenTypes(){
        String accessToken = jwtService.createJwt("some@email.com", TokenType.ACCESS, Role.USER);
        String refreshToken = jwtService.createJwt("some@email.com", TokenType.REFRESH, Role.USER);
        assertThat(jwtService.getTypeFromToken(accessToken)).isEqualTo(TokenType.ACCESS);
        assertThat(jwtService.getTypeFromToken(refreshToken)).isEqualTo(TokenType.REFRESH);
    }

    @Test
    @DisplayName("Jwt service correctly encodes end decode token types")
    public void JwtServiceCorrectlyEncodesEndDecodesUserEmail(){
        final String mockEmail = "some@email.com";
        String accessToken = jwtService.createJwt("some@email.com", TokenType.ACCESS, Role.USER);
        assertThat(jwtService.getEmailFromToken(accessToken)).isEqualTo(mockEmail);
    }

    @Test
    @DisplayName("Jwt service correctly encodes end decode token types")
    public void JwtServiceCorrectlyEncodesEndDecodesUserRole(){
        String accessToken = jwtService.createJwt("some@email.com", TokenType.ACCESS, Role.USER);
        assertThat(jwtService.getRoleFromToken(accessToken)).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("Jwt service throws exception on forged token")
    public void JwtServiceThrowsExceptionOnForgedToken(){
        String forgedToken = jwtService.createJwt("some@email.com", TokenType.ACCESS, Role.USER).substring(1);
        assertThatCode(() -> jwtService.decodeAndValidateToken(forgedToken)).isInstanceOf(RuntimeException.class);
    }

}
