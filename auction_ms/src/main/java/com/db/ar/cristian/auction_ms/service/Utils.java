package com.db.ar.cristian.auction_ms.service;

import com.db.ar.cristian.auction_ms.model.Auction;
import com.db.ar.cristian.auction_ms.model.AuctionStatus;

import java.time.Instant;

public class Utils {

    public static void refreshStatus(Auction auction, Instant now) {
        if (auction.getStatus() != AuctionStatus.CLOSED) {
            if (!now.isBefore(auction.getEndsAt())) {
                auction.setStatus(AuctionStatus.CLOSED);
            } else if (!now.isBefore(auction.getStartsAt())) {
                auction.setStatus(AuctionStatus.OPEN);
            } else {
                auction.setStatus(AuctionStatus.SCHEDULED);
            }
        }
    }
}
