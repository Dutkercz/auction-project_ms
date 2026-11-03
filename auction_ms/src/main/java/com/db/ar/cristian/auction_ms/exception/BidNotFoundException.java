package com.db.ar.cristian.auction_ms.exception;

public class BidNotFoundException extends RuntimeException {
    public BidNotFoundException(String message) {
        super(message);
    }
}
