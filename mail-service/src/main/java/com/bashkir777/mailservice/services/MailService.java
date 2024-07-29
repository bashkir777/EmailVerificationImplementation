package com.bashkir777.mailservice.services;

import com.bashkir777.mailservice.enums.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    @Getter
    private String senderEmail;

    @Async
    public CompletableFuture<Void> sendConfirmationEmail(String to,
                                                   String firstname,
                                                   String activationCode
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                UTF_8.name());

        Map<String, Object> properties = new HashMap<>();
        properties.put("firstname", firstname);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        mimeMessageHelper.setFrom(senderEmail);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(EmailTemplateName.CONFIRMATION_EMAIL.getSubject());

        String template
                = templateEngine.process(EmailTemplateName.CONFIRMATION_EMAIL.getName(), context);

        mimeMessageHelper.setText(template, true);

        mailSender.send(mimeMessage);

        return CompletableFuture.completedFuture(null);
    }
}
