package com.integration.config.channels;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;

@Configuration
@Slf4j
@EnableIntegration
public class RendezvousChannelConfig {

    @Bean
    public RendezvousChannel rendezvousChannel(){
        return new RendezvousChannel();
    }

    @ServiceActivator(inputChannel = "rendezvousChannel")
    public String rendezvousChannel(Message<?> message){
        String payload = (String) message.getPayload();
        log.info("Received payload form rendezvousChannel: {}", payload);
        return "OK";
    }

}
