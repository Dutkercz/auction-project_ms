package com.db.ar.cristian.auction_ms.service;

import com.db.ar.cristian.auction_ms.dto.AuctionResponse;
import com.db.ar.cristian.auction_ms.dto.CreateAuctionRequest;
import com.db.ar.cristian.auction_ms.model.AuctionStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuctionServiceTests {

    @Autowired
    private AuctionService auctionService;

    @Test
    void createsAndListsAuctionWithOpenStatusWhenStartTimeIsOmitted() throws JsonProcessingException {
        AuctionResponse auction = auctionService.createAuction(new CreateAuctionRequest(
                "  Notebook  ",
                "Notebook usado",
                new BigDecimal("100.00"),
                1L,
                null,
                360L
        ));

        assertEquals("Notebook", auction.title());
        assertEquals(new BigDecimal("100.00"), auction.minimumBid());
        assertEquals(AuctionStatus.OPEN, auction.status());
        assertEquals(auction.id(), auctionService.findAuction(auction.id()).id());
        assertEquals(List.of(auction.id()), auctionService.findAllAuctions().stream()
                .map(AuctionResponse::id)
                .toList());
    }

    @Test
    void schedulesAuctionThatStartsInTheFuture() throws JsonProcessingException {
        Instant startsAt = Instant.now().plus(1, ChronoUnit.HOURS);
        AuctionResponse auction = auctionService.createAuction(new CreateAuctionRequest(
                "Console",
                null,
                new BigDecimal("50.00"),
                1L,
                startsAt,
                360L
        ));

        assertEquals(AuctionStatus.SCHEDULED, auction.status());
        assertEquals(AuctionStatus.SCHEDULED, auctionService.findAuction(auction.id()).status());
    }

    @Test
    void rejectsAuctionWhoseEndTimeDoesNotFollowItsStartTime() {
        Instant start = Instant.now().plus(1, ChronoUnit.HOURS);
        assertThrows(ResponseStatusException.class, () -> auctionService.createAuction(
                new CreateAuctionRequest(
                        "Console",
                        null,
                        new BigDecimal("50.00"),
                        1L,
                        start,
                        360L
                )
        ));
    }

}
