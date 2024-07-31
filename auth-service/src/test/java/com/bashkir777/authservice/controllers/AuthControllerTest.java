package com.bashkir777.authservice.controllers;

import com.bashkir777.authservice.data.dao.UserService;
import com.bashkir777.authservice.data.entities.OTPToken;
import com.bashkir777.authservice.dto.RegisterRequest;
import com.bashkir777.authservice.dto.TokenPair;
import com.bashkir777.authservice.dto.VerificationRequest;
import com.bashkir777.authservice.services.AuthenticationService;
import com.bashkir777.authservice.services.JwtService;
import com.bashkir777.authservice.services.OTPService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    private AuthenticationService authenticationService;
    @Autowired
    private JwtService jwtService;

    @Test
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
