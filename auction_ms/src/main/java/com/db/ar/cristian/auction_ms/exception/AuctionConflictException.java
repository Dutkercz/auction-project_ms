package com.db.ar.cristian.auction_ms.exception;

public class AuctionConflictException extends RuntimeException {
    public AuctionConflictException(String message) {
        super(message);
    }
}
