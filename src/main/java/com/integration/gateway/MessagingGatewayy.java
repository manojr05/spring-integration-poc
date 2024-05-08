package com.integration.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "myGateway", defaultRequestChannel = "rendezvousChannel")
public interface MessagingGatewayy {
    @Gateway(requestChannel = "rendezvousChannel")
    String echo(String payload);

}
