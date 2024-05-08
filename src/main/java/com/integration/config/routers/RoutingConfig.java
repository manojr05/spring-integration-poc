package com.integration.config.routers;

import com.integration.model.Employee;
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
public class RoutingConfig {

    @Bean
    public Employee employee(){
        return new Employee();
    }

    @Bean
    public MessageChannel defaultChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel channelA(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel channelB(){
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow routingConfigHttpFlow(){
        return IntegrationFlow.from(Http.inboundGateway("/routingConfigHttpFlow")
                .requestMapping(r -> r.methods(HttpMethod.POST))
                .requestPayloadType(String.class))
                .route(new Function<Message<?>, String>() {
                    @Override
                    public String apply(Message<?> message) {
                        String payload =(String) message.getPayload();
                        if(payload.contains("MALE"))
                            return "channelA";
                        else if(payload.contains("FEMALE"))
                            return "channelB";
                        return "defaultChannel";
                    }
                })
                .get();
    }

    @Bean
    public IntegrationFlow channelAFlow() {
        return IntegrationFlow.from("channelA")
                .handle(routingConfigHttpFlowHandler())
                .get();
    }

    @Bean
    public IntegrationFlow channelBFlow() {
        return IntegrationFlow.from("channelB")
                .handle(routingConfigHttpFlowHandler())
                .get();
    }

    @Bean
    public IntegrationFlow defaultChannelFlow() {
        return IntegrationFlow.from("defaultChannel")
                .handle(routingConfigHttpFlowHandler())
                .get();
    }

    @Bean
    public Function<Message<?>, String> routingConfigHttpFlowHandler() {
        return new Function<Message<?>, String>() {
            public String apply(Message<?> message) throws MessagingException {
                log.info("Received message: {}", message.getPayload());
                return "OK";
            }
        };
    }
}

