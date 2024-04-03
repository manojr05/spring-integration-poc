package com.integration.controller;

import com.integration.gateway.MessagingGatewayy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class IntegrationController {

    private final MessagingGatewayy gateway;

    @PostMapping("/messagingGateway/{message}")
    public String sendMessageToChannel(@PathVariable String message){
        return gateway.echo(message);
    }

}
