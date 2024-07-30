package com.bashkir777.authservice.controllers;

import com.bashkir777.authservice.data.dao.UserService;
import com.bashkir777.authservice.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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

    @Test
    public void userSuccessfullyRegistered() throws Exception {
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

    }
}
