package com.db.ar.cristian.auction_ms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuctionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsAndRetrievesAuction() throws Exception {
        String location = mockMvc.perform(post("/api/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(auctionRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Console"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andReturn()
                .getResponse()
                .getHeader("Location");
        String auctionId = auctionIdFrom(location);

        mockMvc.perform(get("/api/auctions/{auctionId}", auctionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Integer.parseInt(auctionId)))
                .andExpect(jsonPath("$.finalPrice").value(100.00));

        mockMvc.perform(get("/api/auctions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(Integer.parseInt(auctionId)));
    }

    @Test
    void rejectsInvalidAuctionRequest() throws Exception {
        mockMvc.perform(post("/api/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": " ",
                                  "initialPrice": -1,
                                  "sellerId": 0,
                                  "endsAt": "2030-01-01T00:00:00Z"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createsAndListsBidUsingBidController() throws Exception {
        String location = mockMvc.perform(post("/api/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(auctionRequest()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");
        String auctionId = auctionIdFrom(location);

        mockMvc.perform(post("/api/v1/bids/{auctionId}/bids", auctionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 125.00,
                                  "bidderId": 2
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.auctionId").value(Integer.parseInt(auctionId)))
                .andExpect(jsonPath("$.amount").value(125.00))
                .andExpect(jsonPath("$.bidderId").value(2));

        mockMvc.perform(get("/api/v1/bids/{auctionId}/bids", auctionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(125.00));
    }

    private String auctionRequest() {
        Instant now = Instant.now();
        return """
                {
                  "title": "Console",
                  "description": "Console usada",
                  "initialPrice": 100.00,
                  "sellerId": 1,
                  "startsAt": "%s",
                  "endsAt": "%s"
                }
                """.formatted(now.minusSeconds(30), now.plusSeconds(3600));
    }

    private String auctionIdFrom(String location) {
        return location.substring(location.lastIndexOf('/') + 1);
    }
}
