package com.bashkir777.authservice.services;

import com.bashkir777.authservice.dto.ConfirmationMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;


@Service
@RequiredArgsConstructor
public class ConfirmationProducer {
    private final RabbitTemplate template;
    private final String EXCHANGE = "confirmationExchange";
    private final String ROUTING_KEY = "";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void produceMessage(ConfirmationMessage confirmationMessage) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(confirmationMessage);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        Message message = new Message(jsonMessage.getBytes(), messageProperties);
        template.send(EXCHANGE, ROUTING_KEY, message);
    }

}
