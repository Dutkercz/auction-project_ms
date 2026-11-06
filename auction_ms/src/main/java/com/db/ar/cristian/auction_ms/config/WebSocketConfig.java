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
        //habiilitar o Broker em memória para distribuir mensagens
        // Clientes vão escutar canais que começam com "/topic" (ex: /topic/auction/123)
        registry.enableSimpleBroker("/topic");
        // Prefixo para mensagens que o cliente ENVIA ao servidor (não usarei, pois o lance vai via HTTP REST)
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // O endpoint onde o frontend vai tentar conectar o WebSocket inicialmente
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:5173");
    }
}
