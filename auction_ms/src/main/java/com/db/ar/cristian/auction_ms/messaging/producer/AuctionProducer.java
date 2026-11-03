package com.db.ar.cristian.auction_ms.messaging.producer;

import com.db.ar.cristian.auction_ms.dto.BidResponse;
import com.db.ar.cristian.auction_ms.model.Bid;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendNewAuction(AuctionProducerDto producerDto) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(producerDto);
        kafkaTemplate.send("auction_create", producerDto.id().toString(), json);
        log.info("Enviado mensagem de criação de Leilão {}", producerDto.id());
    }

    public void newAuctionBid(BidResponse bidResponse) {
        String json = objectMapper.writeValueAsString(bidResponse);
        kafkaTemplate.send("auction.bid", bidResponse.auctionId().toString(), json);
    }
}
