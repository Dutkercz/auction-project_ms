package com.db.ar.cristian.auction_ms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateBidRequest(
        @NotNull @Positive BigDecimal amount,
        @NotNull @Positive Long bidderId
) {
}
