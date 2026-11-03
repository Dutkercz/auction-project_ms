package com.db.ar.cristian.auction_ms.mapper;

import com.db.ar.cristian.auction_ms.dto.BidResponse;
import com.db.ar.cristian.auction_ms.dto.CreateBidRequest;
import com.db.ar.cristian.auction_ms.model.Auction;
import com.db.ar.cristian.auction_ms.model.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface BidMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "auction", source = "auction")
    @Mapping(target = "createdAt", source = "now")
    Bid toEntity(CreateBidRequest request, Auction auction, Instant now);

    @Mapping(source = "bid.auction.id", target = "auctionId")
    BidResponse toResponse(Bid bid);
}
