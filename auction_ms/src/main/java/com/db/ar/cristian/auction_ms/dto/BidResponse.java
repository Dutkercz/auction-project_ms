package com.db.ar.cristian.auction_ms.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record BidResponse(
        Long id,
        Long auctionId,
        BigDecimal amount,
        Long bidderId,
        Instant createdAt) {
}
