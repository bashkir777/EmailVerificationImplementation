package com.bashkir777.mailservice;

import com.bashkir777.mailservice.enums.EmailTemplateName;
import com.bashkir777.mailservice.services.MailService;
import jakarta.mail.MessagingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class MailServiceApplication {

    public static void main(String[] args) throws MessagingException {

        ApplicationContext context = SpringApplication.run(MailServiceApplication.class, args);

        MailService mailService = context.getBean(MailService.class);

        mailService.sendConfirmationEmail("supletsovd@gmail.com", "firstname", "123456");
    }

}
