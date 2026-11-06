package com.db.ar.cristian.auction_ms.service.validation;

import com.db.ar.cristian.auction_ms.dto.CreateBidRequest;
import com.db.ar.cristian.auction_ms.exception.BidUnexpectedException;
import com.db.ar.cristian.auction_ms.model.Auction;
import org.springframework.stereotype.Component;

@Component
public class ValidateAuctionBidBidder implements AuctionBidValidation {
    @Override
    public void validateAuctionBid(Auction auction, CreateBidRequest request) {
        if (auction.getSellerId().equals(request.bidderId())) {
            throw new BidUnexpectedException("O vendedor não pode dar lances no próprio leilão.");
        }
    }
}
