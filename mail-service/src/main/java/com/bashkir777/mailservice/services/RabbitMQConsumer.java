package com.bashkir777.mailservice.services;


import com.bashkir777.mailservice.dto.ConfirmationMessage;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final MailService mailService;

    @RabbitListener(queues = "confirmationQueue")
    public void receiveConfirmationMessage(ConfirmationMessage confirmationMessage) throws MessagingException {
        mailService.sendConfirmationEmail(confirmationMessage.getEmail(),
                confirmationMessage.getFirstname(),
                confirmationMessage.getOtp());
    }
}
