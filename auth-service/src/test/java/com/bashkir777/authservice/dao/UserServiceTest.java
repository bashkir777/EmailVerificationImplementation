package com.bashkir777.authservice.dao;

import com.bashkir777.authservice.data.dao.RefreshTokenService;
import com.bashkir777.authservice.data.dao.UserService;
import com.bashkir777.authservice.data.entities.RefreshToken;
import com.bashkir777.authservice.data.entities.User;
import com.bashkir777.authservice.services.OTPService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {


    private UserService userService;
    private OTPService otpService;
    private RefreshTokenService refreshTokenService;

    private final String MOCK_EMAIL = "some@gmail.com";

    @Autowired
    private void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private void setRefreshTokenService(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Autowired
    private void setUserService(OTPService otpService) {
        this.otpService = otpService;
    }

    @Test
    @Sql(scripts = "/sql/createDisabledUser.sql")
    public void userSuccessfullyDeleted() {
        userService.deleteUserByEmail(MOCK_EMAIL);
        assertThatCode(() -> userService.getUserByEmail(MOCK_EMAIL))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @Transactional
    @Sql(scripts = "/sql/createDisabledUser.sql")
    public void userSuccessfullyDeleted_WhenItHasDependencies() {

        User user = userService.getUserByEmail(MOCK_EMAIL);

        otpService.saveOtpToken(MOCK_EMAIL, otpService.generateOtp());

        refreshTokenService.saveRefreshToken(RefreshToken.builder()
                .user(user)
                .refreshToken("sometoken")
                .build());

        assertThatCode(() -> userService.deleteUserByEmail(MOCK_EMAIL))
                .doesNotThrowAnyException();
    }

}
