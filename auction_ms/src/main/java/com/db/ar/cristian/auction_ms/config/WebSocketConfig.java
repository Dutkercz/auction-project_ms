package com.db.ar.cristian.auction_ms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//O STOMP (Simple Text Oriented Messaging Protocol ou Streaming Text Oriented Messaging Protocol)
// é um protocolo de mensagens leve, baseado em texto e estruturado de forma semelhante ao HTTP.
// Ele permite a comunicação interoperável entre diferentes clientes e brokers (intermediários) de mensagens
@Configuration
@EnableWebSocketMessageBroker //habilitar o suporte a STOMP sobre websocket (
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); //habiilitar o Broker para distribuir mensagens
        registry.setApplicationDestinationPrefixes("/app"); //define destino de mensagens à aplicação
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")//define o endereço de conexão websocket
                .setAllowedOriginPatterns("http://localhost:5173");
    }
}
