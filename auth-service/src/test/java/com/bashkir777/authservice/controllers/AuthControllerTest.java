package com.bashkir777.authservice.controllers;

import com.bashkir777.authservice.data.dao.RefreshTokenService;
import com.bashkir777.authservice.data.dao.UserService;
import com.bashkir777.authservice.data.entities.OTPToken;
import com.bashkir777.authservice.data.entities.RefreshToken;
import com.bashkir777.authservice.data.entities.User;
import com.bashkir777.authservice.dto.*;
import com.bashkir777.authservice.services.AuthenticationService;
import com.bashkir777.authservice.services.JwtService;
import com.bashkir777.authservice.services.OTPService;
import com.bashkir777.authservice.services.enums.Role;
import com.bashkir777.authservice.services.enums.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserService userService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;


    @Test
    @SqlGroup(
            {
                    @Sql(scripts = "/sql/createEnabledUser.sql"
                            , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),

                    @Sql(scripts = "/sql/truncateUser.sql"
                            , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
            }
    )
    public void resetPasswordSuccessful() throws Exception {
        final String MOCK_EMAIL = "some@gmail.com";
        final String MOCK_PASSWORD = "password";
        final String MOCK_NEW_PASSWORD = "new_password";
        var loginRequest = LoginRequest.builder().email(MOCK_EMAIL)
                .password(MOCK_PASSWORD).build();

        authenticationService.login(loginRequest);

        OTPToken[] otpToken = new OTPToken[1];

        assertThatCode(() ->
                otpToken[0] = otpService.getOtpTokenByUser(userService.getUserByEmail(MOCK_EMAIL))
        ).doesNotThrowAnyException();

        ResetPassword resetRequest = ResetPassword.builder()
                .newPassword(MOCK_NEW_PASSWORD)
                .email(MOCK_EMAIL)
                .otp(otpToken[0].getOtp())
                .build();

        String requestBody = objectMapper.writeValueAsString(resetRequest);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isOk()).andReturn();


        TokenPair tokenPair = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                TokenPair.class
        );

        assertThat(passwordEncoder.matches(
                    MOCK_NEW_PASSWORD,
                    userService.getUserByEmail(MOCK_EMAIL).getPassword()
                )
        ).isTrue();

        assertThatCode(() -> {
                jwtService.decodeAndValidateToken(tokenPair.getRefreshToken());
                jwtService.decodeAndValidateToken(tokenPair.getAccessToken());
        }
        ).doesNotThrowAnyException();
    }


    @Test
    @SqlGroup(
            {
                    @Sql(scripts = "/sql/createEnabledUser.sql"
                            , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),

                    @Sql(scripts = "/sql/truncateUser.sql"
                            , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
            }
    )
    @Transactional
    public void logoutSuccessful() throws Exception {
        final String MOCK_EMAIL = "some@gmail.com";

        User user = userService.getUserByEmail(MOCK_EMAIL);

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(jwtService.createJwt(MOCK_EMAIL, TokenType.REFRESH, Role.USER))
                .user(user)
                .build();

        refreshTokenService.saveRefreshToken(refreshToken);

        var logoutRequest = RefreshTokenDTO
                .builder()
                .refreshToken(refreshToken.getRefreshToken())
                .build();

        String requestBody = objectMapper.writeValueAsString(logoutRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isOk());

        assertThatCode(
                () -> refreshTokenService.getRefreshTokenByUser(user)
        ).isInstanceOf(BadCredentialsException.class);

    }


    @Test
    @SqlGroup(
            {
                @Sql(scripts = "/sql/createEnabledUser.sql"
                            , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),

                @Sql(scripts = "/sql/truncateUser.sql"
                            , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
            }
    )
    public void loginSuccessful() throws Exception {
        final String MOCK_EMAIL = "some@gmail.com";
        final String MOCK_PASSWORD = "password";

        var loginRequest = LoginRequest.builder().email(MOCK_EMAIL)
                .password(MOCK_PASSWORD).build();

        String requestBody = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isOk());

        OTPToken[] otpToken = new OTPToken[1];

        assertThatCode(() ->
                otpToken[0] = otpService.getOtpTokenByUser(userService.getUserByEmail(MOCK_EMAIL))
        ).doesNotThrowAnyException();

        assertThat(otpToken[0].getOtp()).hasSize(6);

    }

    @Test
    @Sql(scripts = "/sql/truncateUser.sql"
            , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void otpVerificationSuccessful() throws Exception {
        final String MOCK_EMAIL = "some@gmail.com";
        var registerRequest = RegisterRequest.builder().email(MOCK_EMAIL)
                .password("password")
                .firstname("firstname")
                .lastname("lastname").build();
        authenticationService.register(registerRequest);

        OTPToken otpToken = otpService.getOtpTokenByUser(
                userService.getUserByEmail(MOCK_EMAIL)
        );

        var verificationRequest = VerificationRequest.builder().email(MOCK_EMAIL)
                .otp(otpToken.getOtp()).build();

        String requestBody = objectMapper.writeValueAsString(verificationRequest);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isOk()).andReturn();

        TokenPair[] tokenPair = new TokenPair[1];

        assertThatCode(() ->
                tokenPair[0] = objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), TokenPair.class)
        ).doesNotThrowAnyException();


        assertThatCode(() ->
                jwtService.decodeAndValidateToken(tokenPair[0].getAccessToken())
        ).doesNotThrowAnyException();

        assertThatCode(() ->
                jwtService.decodeAndValidateToken(tokenPair[0].getRefreshToken())
        ).doesNotThrowAnyException();

    }

    @Test
    @Transactional
    @SqlGroup(
            {
                    @Sql(scripts = "/sql/createEnabledUser.sql"
                            , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),

                    @Sql(scripts = "/sql/truncateUser.sql"
                            , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
            }
    )
    public void refreshTokenSuccessfully() throws Exception {
        final String MOCK_EMAIL = "some@gmail.com";

        User user = userService.getUserByEmail(MOCK_EMAIL);

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(jwtService.createJwt(MOCK_EMAIL, TokenType.REFRESH, Role.USER))
                .user(user)
                .build();

        refreshTokenService.saveRefreshToken(refreshToken);

        refreshTokenService.getRefreshTokenByUser(user);

        RefreshTokenDTO refreshTokenDTO = RefreshTokenDTO.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .build();

        String requestBody = objectMapper.writeValueAsString(refreshTokenDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isOk()).andReturn();
        AccessToken[] accessToken = new AccessToken[1];



        assertThatCode(() ->
                accessToken[0] = objectMapper
                        .readValue(
                                mvcResult.getResponse().getContentAsString()
                                , AccessToken.class
                        )
        ).doesNotThrowAnyException();

        assertThatCode(() -> jwtService.decodeAndValidateToken(accessToken[0].getAccessToken())
        ).doesNotThrowAnyException();

    }

    @Test
    @Sql(scripts = "/sql/truncateUser.sql"
            , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void userSuccessfullyRegisteredAndOtpSaved() throws Exception {
        final String MOCK_EMAIL = "some@gmail.com";
        var request = RegisterRequest.builder().email(MOCK_EMAIL)
                .password("password")
                .firstname("firstname")
                .lastname("lastname").build();


        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isCreated());

        assertThatCode(() -> userService.getUserByEmail(MOCK_EMAIL))
                .doesNotThrowAnyException();
        assertThatCode(() -> otpService
                .getOtpTokenByUser(userService.getUserByEmail(MOCK_EMAIL))
        ).doesNotThrowAnyException();
    }

    @Test
    public void invalidEmailDoesNotRegistered() throws Exception {
        final String INVALID_EMAIL = "someinvalidgmail.com";
        var request = RegisterRequest.builder().email(INVALID_EMAIL)
                .password("password")
                .firstname("firstname")
                .lastname("lastname").build();


        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isBadRequest());
    }

}
