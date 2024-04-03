package com.integration.config.channels;

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
import org.springframework.messaging.MessagingException;

import java.util.function.Function;

@Slf4j
@Configuration
@EnableIntegration
public class DirectChannelConfig {

    @Bean
    public MessageChannel directChannel(){
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow directChannelHttpFlow(){
        return IntegrationFlow.from(Http.inboundGateway("/directChannel")
                .requestMapping(r -> r.methods(HttpMethod.POST))
                .requestPayloadType(String.class))
                .channel("directChannel")
                .handle(directChannelHttpFlowHandler())
                .get();
    }

    @Bean
    public Function<Message<?>, String> directChannelHttpFlowHandler() {
        return new Function<Message<?>, String>() {
            public String apply(Message<?> message) throws MessagingException {
                log.info("Received message: {}", message.getPayload());
                return "OK";
            }
        };

    }
}

