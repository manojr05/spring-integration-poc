package com.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;

import java.util.function.Function;

@Configuration
@Slf4j
public class PointToPointChannelConfig {

    @Bean
    public MessageChannel pointToPointChannel() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow pointToPointChannelHttpFlow(){
        return IntegrationFlow.from(Http.inboundGateway("/pointToPointChannel")
                        .requestMapping(r -> r.methods(HttpMethod.POST))
                        .requestPayloadType(String.class))
                .channel("pointToPointChannel")
                .handle(pointToPointChannelHttpFlowHandler())
                .get();
    }

    @Bean
    public Function<Message<?>, String> pointToPointChannelHttpFlowHandler() {
        return new Function<Message<?>, String>() {
            public String apply(Message<?> message) throws MessagingException {
                log.info("Point-to-Point Channel Received Message: " + message.getPayload());
                return "OK";
            }
        };
    }

//    @Bean
//    public IntegrationFlow pointToPointFlow() {
//        return IntegrationFlow.from("pointToPointChannel")
//                .handle(message -> log.info("Point-to-Point Channel Received Message: " + message.getPayload()))
//                .get();
//    }

}
