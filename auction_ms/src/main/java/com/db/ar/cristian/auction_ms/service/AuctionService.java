package com.db.ar.cristian.auction_ms.service;

import com.db.ar.cristian.auction_ms.dto.AuctionResponse;
import com.db.ar.cristian.auction_ms.dto.BidUpdateDto;
import com.db.ar.cristian.auction_ms.dto.CreateAuctionRequest;
import com.db.ar.cristian.auction_ms.exception.AuctionUnexpectedException;
import com.db.ar.cristian.auction_ms.mapper.AuctionMapper;
import com.db.ar.cristian.auction_ms.messaging.event.AuctionEventListener;
import com.db.ar.cristian.auction_ms.messaging.producer.AuctionProducer;
import com.db.ar.cristian.auction_ms.model.Auction;
import com.db.ar.cristian.auction_ms.model.AuctionStatus;
import com.db.ar.cristian.auction_ms.repository.AuctionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final AuctionMapper auctionMapper;
    private final AuctionProducer auctionProducer;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public AuctionResponse createAuction(CreateAuctionRequest request) throws JsonProcessingException {
        Instant now = Instant.now();
        Instant startsAt = request.startsAt() == null ? now : request.startsAt();
        Instant endsAt = startsAt.plusSeconds(request.duration());
        if (endsAt.isBefore(startsAt)) {
            throw new AuctionUnexpectedException("O encerramento deve ocorrer após o início.");
        }
        if (endsAt.isBefore(now)) {
            throw new AuctionUnexpectedException("O leilão deve terminar no futuro.");
        }

        Auction auction = auctionMapper.toEntity(request, startsAt, now, endsAt);
        auction.setStatus(startsAt.isAfter(now) ? AuctionStatus.SCHEDULED : AuctionStatus.OPEN);
        auctionRepository.save(auction);
        //envia evento de novo auction aos consumidores
        auctionProducer.sendNewAuction(auctionMapper.toAuctionMessage(auction));
        //envia evento interno ao listener
        eventPublisher.publishEvent(new BidUpdateDto(auction.getId(), auction.getMinimumBid()));
        return auctionMapper.toResponse(auction);
    }

    @Transactional
    public List<AuctionResponse> findAllAuctions() {
        Instant now = Instant.now();
        return auctionRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(auction -> {
                    Utils.refreshStatus(auction, now);
                    return auctionMapper.toResponse(auction);
                })
                .toList();
    }

    @Transactional
    public AuctionResponse findAuction(Long auctionId) {
        Auction auction = getAuction(auctionId);
        Utils.refreshStatus(auction, Instant.now());
        return auctionMapper.toResponse(auction);
    }

    private Auction getAuction(Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(
                () -> new AuctionUnexpectedException("Leilão não encontrado com id: " +auctionId));
    }


}
