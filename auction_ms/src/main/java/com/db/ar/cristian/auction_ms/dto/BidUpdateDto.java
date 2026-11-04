package com.db.ar.cristian.auction_ms.dto;

import java.math.BigDecimal;

public record BidUpdateDto(
        Long auctionId,
        BigDecimal actualBid
) {
}
