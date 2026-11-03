package com.db.ar.cristian.auction_ms.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auctions")
@Getter
@Setter
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal minimumBid;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal actualBid;

    @Column(precision = 19, scale = 2)
    private BigDecimal winnerBid;

    @Column(nullable = false)
    private Long sellerId;

    private Long winnerId;

    @Column(nullable = false)
    private Instant startsAt;

    @Column(nullable = false)
    private Instant endsAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private AuctionStatus status;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bid> bids = new ArrayList<>();

    public void addBid(Bid bid){
        bids.add(bid);
        bid.setAuction(this);
    }

    public void removeBid(Bid bid){
        bids.remove(bid);
        bid.setAuction(null);
    }
}
