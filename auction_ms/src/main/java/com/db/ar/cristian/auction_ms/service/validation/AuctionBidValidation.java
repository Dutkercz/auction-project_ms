package com.db.ar.cristian.auction_ms.service.validation;

import com.db.ar.cristian.auction_ms.dto.CreateBidRequest;
import com.db.ar.cristian.auction_ms.model.Auction;

public interface AuctionBidValidation {
    void validateAuctionBid(Auction auction, CreateBidRequest request);
}
