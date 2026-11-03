package com.db.ar.cristian.auction_ms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BidNotFoundException.class, AuctionNotFoundException.class, UnexpectedFeignException.class})
    public ResponseEntity<?> notFoundHandler(RuntimeException e){
        var errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetail);
    }

    @ExceptionHandler({BidUnexpectedException.class, AuctionUnexpectedException.class})
    public ResponseEntity<?> unexpectedHandler(RuntimeException e){
        var errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    @ExceptionHandler(AuctionConflictException.class)
    public ResponseEntity<?> conflictException(RuntimeException e){
        var errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetail);
    }

}
