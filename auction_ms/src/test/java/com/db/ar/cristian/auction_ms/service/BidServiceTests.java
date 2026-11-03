//package com.db.ar.cristian.auction_ms.service;
//
//import com.db.ar.cristian.auction_ms.dto.AuctionResponse;
//import com.db.ar.cristian.auction_ms.dto.BidResponse;
//import com.db.ar.cristian.auction_ms.dto.CreateAuctionRequest;
//import com.db.ar.cristian.auction_ms.dto.CreateBidRequest;
//import com.db.ar.cristian.auction_ms.model.AuctionStatus;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//class BidServiceTests {
//
//    @Autowired
//    private AuctionService auctionService;
//
//    @Autowired
//    private BidService bidService;
//
//    @Test
//    void acceptsIncreasingBidsAndUpdatesAuctionWinnerAndPrice() throws JsonProcessingException {
//        AuctionResponse auction = createOpenAuction();
//
//        BidResponse firstBid = bidService.placeBid(
//                auction.id(),
//                new CreateBidRequest(new BigDecimal("125.00"), 2L)
//        );
//        BidResponse secondBid = bidService.placeBid(
//                auction.id(),
//                new CreateBidRequest(new BigDecimal("150.00"), 3L)
//        );
//
//        AuctionResponse updated = auctionService.findAuction(auction.id());
//        assertEquals(new BigDecimal("150.00"), updated.());
//        assertEquals(3L, updated.winnerId());
//        assertEquals(auction.id(), firstBid.auctionId());
//        assertEquals(auction.id(), secondBid.auctionId());
//        List<Long> bidIds = bidService.findBids(auction.id()).stream()
//                .map(BidResponse::id)
//                .toList();
//        assertEquals(2, bidIds.size());
//        assertTrue(bidIds.contains(firstBid.id()));
//        assertTrue(bidIds.contains(secondBid.id()));
//    }
//
//    @Test
//    void rejectsBidThatDoesNotExceedCurrentPrice() throws JsonProcessingException {
//        AuctionResponse auction = createOpenAuction();
//        bidService.placeBid(auction.id(), new CreateBidRequest(new BigDecimal("125.00"), 2L));
//
//        ResponseStatusException error = assertThrows(ResponseStatusException.class, () ->
//                bidService.placeBid(auction.id(), new CreateBidRequest(new BigDecimal("125.00"), 3L)));
//
//        assertEquals(400, error.getStatusCode().value());
//    }
//
//    @Test
//    void rejectsSellerBiddingOnOwnAuction() throws JsonProcessingException {
//        AuctionResponse auction = createOpenAuction();
//
//        ResponseStatusException error = assertThrows(ResponseStatusException.class, () ->
//                bidService.placeBid(auction.id(), new CreateBidRequest(new BigDecimal("125.00"), 1L)));
//
//        assertEquals(400, error.getStatusCode().value());
//    }
//
//    @Test
//    void rejectsBidsBeforeAuctionStarts() throws JsonProcessingException {
//        Instant startsAt = Instant.now().plus(1, ChronoUnit.HOURS);
//        AuctionResponse auction = auctionService.createAuction(new CreateAuctionRequest(
//                "Console",
//                null,
//                new BigDecimal("100.00"),
//                1L,
//                startsAt,
//                360L)
//        );
//
//        ResponseStatusException error = assertThrows(ResponseStatusException.class, () ->
//                bidService.placeBid(auction.id(), new CreateBidRequest(new BigDecimal("125.00"), 2L)));
//
//        assertEquals(AuctionStatus.SCHEDULED, auction.status());
//        assertEquals(409, error.getStatusCode().value());
//    }
//
//    @Test
//    void rejectsBidsForUnknownAuction() {
//        assertThrows(EntityNotFoundException.class, () -> bidService.placeBid(
//                Long.MAX_VALUE,
//                new CreateBidRequest(new BigDecimal("125.00"), 2L)
//        ));
//    }
//
//    private AuctionResponse createOpenAuction() throws JsonProcessingException {
//        Instant now = Instant.now();
//        return auctionService.createAuction(new CreateAuctionRequest(
//                "Notebook",
//                "Notebook usado",
//                new BigDecimal("100.00"),
//                1L,
//                now,
//                360L
//        ));
//    }
//}
