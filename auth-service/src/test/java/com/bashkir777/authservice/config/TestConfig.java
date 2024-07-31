package com.bashkir777.authservice.config;


import com.bashkir777.authservice.dto.ConfirmationMessage;
import com.bashkir777.authservice.services.ConfirmationProducer;
import static org.mockito.Mockito.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    private ConfirmationProducer confirmationProducer;

    public TestConfig() throws Exception {}

    {
        confirmationProducer = mock(ConfirmationProducer.class);
        doNothing().when(confirmationProducer).produceMessage(any(ConfirmationMessage.class));
    }

    @Bean
    public ConfirmationProducer confirmationProducer() {
        return confirmationProducer;
    }

}
