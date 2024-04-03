package com.integration.config.transformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integration.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;

import java.util.function.Function;

@Slf4j
@Configuration
@EnableIntegration
public class TransformerConfig {

    @Bean
    public IntegrationFlow transformersHttpFlow(){
        return IntegrationFlow.from(Http.inboundGateway("/transformers")
                .requestMapping(r -> r.methods(HttpMethod.POST))
                        .requestPayloadType(String.class))
                .transform(Transformers.converter(converter()))
                .handle(transformersHttpFlowHandler())
                .get();
    }

    @Bean
    public Converter<String, Employee> converter(){
        return new Converter<>() {
            @Override
            public Employee convert(String source) {
                ObjectMapper objectMapper = new ObjectMapper();
                Employee employee;
                try {
                    employee = objectMapper.readValue(source, Employee.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return employee;
            }
        };

    }


    @Bean
    public Function<Message<?>, String> transformersHttpFlowHandler() {
        return new Function<Message<?>, String>() {
            public String apply(Message<?> message) throws MessagingException {
                log.info("Received message: {}", message.getPayload());
                return "OK";
            }
        };
    }
}

