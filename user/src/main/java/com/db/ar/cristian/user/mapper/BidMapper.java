package com.db.ar.cristian.user.mapper;

import com.db.ar.cristian.user.messaging.AuctionBidsConsumer;
import com.db.ar.cristian.user.model.BidReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BidMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bidId", source = "id")
    @Mapping(target = "auctionId", source = "representation.auctionId")
    @Mapping(target = "user", ignore = true)
    BidReference toReference(AuctionBidsConsumer.BidRepresentation representation);
}
