package com.db.ar.cristian.auction_ms.messaging.producer;

import java.math.BigDecimal;
import java.time.Instant;

public record AuctionProducerDto(
        Long id,
        Long sellerId,
        BigDecimal initialPrice,
        Instant createdAt,
        Instant startsAt,
        Instant endsAt
) {
}
