package com.db.ar.cristian.auction_ms.service.validation;

import com.db.ar.cristian.auction_ms.dto.CreateBidRequest;
import com.db.ar.cristian.auction_ms.exception.BidUnexpectedException;
import com.db.ar.cristian.auction_ms.model.Auction;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ValidateAuctionBidOwner implements AuctionBidValidation{
    @Override
    public void validateAuctionBid(Auction auction, CreateBidRequest request) {
        if (!auction.getBids().isEmpty() &&
            Objects.equals(request.bidderId(), auction.getBids().getLast().getBidderId())){
            throw new BidUnexpectedException("O lance atual já pertence a este usuário");
        }
    }
}
