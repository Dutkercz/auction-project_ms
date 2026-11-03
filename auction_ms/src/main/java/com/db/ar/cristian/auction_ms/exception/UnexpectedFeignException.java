package com.db.ar.cristian.auction_ms.exception;

public class UnexpectedFeignException extends RuntimeException {
    public UnexpectedFeignException(String message) {
        super(message);
    }
}
