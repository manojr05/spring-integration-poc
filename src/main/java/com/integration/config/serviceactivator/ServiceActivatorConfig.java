package com.integration.config.serviceactivator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;

import java.util.function.Function;

@Configuration
@Slf4j
@EnableIntegration
public class ServiceActivatorConfig {
    @Bean
    public MessageChannel serviceActivatorChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel serviceActivatorOutputChannel(){
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow serviceActivatorChannelFlow(){
        return IntegrationFlow.from(Http.inboundGateway("/serviceActivator")
                        .requestMapping(r -> r.methods(HttpMethod.POST))
                        .requestPayloadType(String.class))
                .channel("serviceActivatorChannel")
                .get();
    }

    @ServiceActivator(inputChannel = "serviceActivatorChannel",
                        outputChannel = "serviceActivatorOutputChannel")
    public String serviceActivatorService(Message<?> message, @Header("contentType") String contentType){
        log.info("Received message with the header: "+contentType);
        String payload = (String) message.getPayload();
        log.info("Received message to the activator I: {}", payload);
        return "OK";
    }

    @ServiceActivator(inputChannel = "serviceActivatorOutputChannel")
    public String serviceActivatorOutputChannelService(Message<?> message){
        String payload = (String) message.getPayload();
        log.info("Received message to the activator II: {}", payload);
        log.info("Sending the received message as a response to the request");
        return payload;
    }
}
