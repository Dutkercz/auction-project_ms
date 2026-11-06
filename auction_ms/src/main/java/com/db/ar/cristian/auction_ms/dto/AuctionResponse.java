package com.db.ar.cristian.auction_ms.dto;

import com.db.ar.cristian.auction_ms.model.AuctionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;

public record AuctionResponse(
        Long id,
        String title,
        String description,
        BigDecimal minimumBid,
        BigDecimal actualBid,
        Long sellerId,

        @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
        Instant startsAt,

        @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
        Instant endsAt,

        @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
        Instant createdAt,

        AuctionStatus status
) {
}
