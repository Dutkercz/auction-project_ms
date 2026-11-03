package com.db.ar.cristian.auction_ms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateAuctionRequest(
        @NotBlank
        String title,

        String description,

        @NotNull @Positive
        BigDecimal minimumBid,

        @NotNull @Positive
        Long sellerId,

        //da pra usar pra fazer um contador regressivo pro incio dos lances
        Instant startsAt,

        @NotNull
        Long duration // seconds
) {
}
