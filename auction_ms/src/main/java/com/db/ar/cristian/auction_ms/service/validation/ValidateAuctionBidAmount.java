package com.db.ar.cristian.auction_ms.service.validation;

import com.db.ar.cristian.auction_ms.dto.CreateBidRequest;
import com.db.ar.cristian.auction_ms.exception.BidUnexpectedException;
import com.db.ar.cristian.auction_ms.model.Auction;
import org.springframework.stereotype.Component;

@Component
public class ValidateAuctionBidAmount implements AuctionBidValidation {
    @Override
    public void validateAuctionBid(Auction auction, CreateBidRequest request) {
        if (request.amount().compareTo(auction.getMinimumBid()) < 0 ||
            (auction.getActualBid() != null && request.amount().compareTo(auction.getActualBid()) <= 0)) {
                throw new BidUnexpectedException("O lance deve ser maior que o atual."
            );
        }
    }
}
