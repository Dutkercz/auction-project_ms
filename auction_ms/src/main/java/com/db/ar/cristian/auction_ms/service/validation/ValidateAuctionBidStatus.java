package com.db.ar.cristian.auction_ms.service.validation;

import com.db.ar.cristian.auction_ms.dto.CreateBidRequest;
import com.db.ar.cristian.auction_ms.exception.AuctionConflictException;
import com.db.ar.cristian.auction_ms.model.Auction;
import com.db.ar.cristian.auction_ms.model.AuctionStatus;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ValidateAuctionBidStatus implements AuctionBidValidation {
    @Override
    public void validateAuctionBid(Auction auction, CreateBidRequest request) {
        if (auction.getStatus() != AuctionStatus.OPEN)
            throw new AuctionConflictException("O leilão não está aberto para lances.");
    }
}
