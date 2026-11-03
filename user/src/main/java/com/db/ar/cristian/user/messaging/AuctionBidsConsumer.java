package com.db.ar.cristian.user.messaging;

import com.db.ar.cristian.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionBidsConsumer {

    private final ObjectMapper objectMapper;
    private final UserService userService;


    @KafkaListener(topics = "auction.bid", groupId = "user.bids")
    public void addUserBid(String json) {
        log.info("Bid event {}", json);
        var bidRepresentation = objectMapper.readValue(json, BidRepresentation.class);
        userService.addUserBidReference(bidRepresentation);
    }

    public record BidRepresentation(
            Long id,
            Long auctionId,
            BigDecimal amount,
            Long bidderId,
            Instant createdAt) {

    }

}
