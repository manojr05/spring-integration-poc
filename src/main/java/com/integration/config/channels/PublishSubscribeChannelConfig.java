package com.integration.config.channels;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;

import java.util.function.Function;

@Configuration
@Slf4j
@EnableIntegration
public class PublishSubscribeChannelConfig {

    @Bean
    public MessageChannel publishSubscribeChannel(){
        return new PublishSubscribeChannel();
    }

    @Bean
    public IntegrationFlow publishSubscribeChannelHttpFlow(){
        return IntegrationFlow.from(Http.inboundGateway("/publishSubscribeChannel")
                        .requestMapping(r -> r.methods(HttpMethod.POST))
                        .requestPayloadType(String.class))
                .channel("publishSubscribeChannel")
                .handle(publishSubscribeChannelHttpFlowHandler())
                .get();
    }

    @Bean
    public Function<Message<?>, String> publishSubscribeChannelHttpFlowHandler() {
        return new Function<Message<?>, String>() {
            public String apply(Message<?> message) throws MessagingException {
                return "OK";
            }
        };
    }

    @Bean
    public IntegrationFlow publishSubscribeFlow1(){
        return IntegrationFlow.from("publishSubscribeChannel")
                .handle(message -> log.info("From pub-sub flow subscriber I: "+message.getPayload()))
                .get();
    }

    @Bean
    public IntegrationFlow publishSubscribeFlow2(){
        return IntegrationFlow.from("publishSubscribeChannel")
                .handle(message -> log.info("From pub-sub flow subscriber II: "+message.getPayload()))
                .get();
    }
}
