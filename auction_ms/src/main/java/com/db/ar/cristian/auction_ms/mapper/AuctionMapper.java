package com.db.ar.cristian.auction_ms.mapper;

import com.db.ar.cristian.auction_ms.dto.AuctionResponse;
import com.db.ar.cristian.auction_ms.dto.CreateAuctionRequest;
import com.db.ar.cristian.auction_ms.messaging.producer.AuctionProducerDto;
import com.db.ar.cristian.auction_ms.model.Auction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;
import java.time.Instant;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuctionMapper {

    @Mapping(source = "startsAt", target = "startsAt")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "endsAt", target = "endsAt")
    Auction toEntity(CreateAuctionRequest request, Instant startsAt, Instant createdAt, Instant endsAt);

    AuctionResponse toResponse(Auction auction);

    AuctionProducerDto toAuctionMessage(Auction auction);
}
