package com.db.ar.cristian.auction_ms.controller;

import com.db.ar.cristian.auction_ms.dto.BidResponse;
import com.db.ar.cristian.auction_ms.dto.CreateBidRequest;
import com.db.ar.cristian.auction_ms.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping("/{auctionId}")
    public ResponseEntity<BidResponse> placeBid(@PathVariable Long auctionId,
                                               @Valid @RequestBody CreateBidRequest request,
                                               UriComponentsBuilder builder) {
        var response = bidService.placeBid(auctionId, request);
        var uri = builder.path("/api/v1/bids/{bidId}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{auctionId}")
    public List<BidResponse> findBids(@PathVariable Long auctionId) {
        return bidService.findBids(auctionId);
    }
}
