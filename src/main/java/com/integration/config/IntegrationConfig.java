package com.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.function.Function;

@Slf4j
@Configuration
@EnableIntegration
public class IntegrationConfig {

    @Bean
    public IntegrationFlow httpFlow(){
        return IntegrationFlow.from(Http.inboundGateway("/demo")
                .requestMapping(r -> r.methods(HttpMethod.POST))
                .requestPayloadType(String.class))
                .handle(handler())
                .get();
    }

    @Bean
    public Function<Message<?>, String> handler(){
        return message -> "ok";
    }

    @Bean
    public MessageChannel directChannel(){
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow directFlow(){
        return IntegrationFlow.from("directChannel")
                .handle(message -> log.info((String) message.getPayload()))
                .get();
    }

}

