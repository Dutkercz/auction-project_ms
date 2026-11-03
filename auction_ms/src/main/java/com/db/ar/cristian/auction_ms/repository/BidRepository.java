package com.db.ar.cristian.auction_ms.repository;

import com.db.ar.cristian.auction_ms.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByAuctionIdOrderByCreatedAtDesc(Long auctionId);
}
