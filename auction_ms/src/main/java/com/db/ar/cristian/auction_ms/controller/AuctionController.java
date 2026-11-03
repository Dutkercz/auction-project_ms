package com.db.ar.cristian.auction_ms.controller;

import com.db.ar.cristian.auction_ms.dto.AuctionResponse;
import com.db.ar.cristian.auction_ms.dto.CreateAuctionRequest;
import com.db.ar.cristian.auction_ms.service.AuctionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<AuctionResponse> createAuction(@Valid @RequestBody CreateAuctionRequest request,
                                                         UriComponentsBuilder builder) throws JsonProcessingException {
        var response = auctionService.createAuction(request);
        var uri = builder.path("/api/auctions/{auctionId}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public List<AuctionResponse> findAllAuctions() {
        return auctionService.findAllAuctions();
    }

    @GetMapping("/{auctionId}")
    public AuctionResponse findAuction(@PathVariable Long auctionId) {
        return auctionService.findAuction(auctionId);
    }
}
