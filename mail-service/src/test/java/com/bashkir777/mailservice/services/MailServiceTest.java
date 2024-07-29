package com.bashkir777.mailservice.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;


    @Test
    @DisplayName("Sending email does not throw an exception")
    public void sendingEmailDoesNotThrowAnException(){
        assertThatCode(
                () -> mailService
                        .sendConfirmationEmail(
                                mailService.getSenderEmail(),
                                mailService.getSenderEmail(),
                                "123455")
                        .get()
        ).doesNotThrowAnyException();
    }
}
