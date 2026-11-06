package com.db.ar.cristian.auction_ms.service;

import com.db.ar.cristian.auction_ms.dto.BidResponse;
import com.db.ar.cristian.auction_ms.dto.BidUpdateDto;
import com.db.ar.cristian.auction_ms.dto.CreateBidRequest;
import com.db.ar.cristian.auction_ms.exception.AuctionConflictException;
import com.db.ar.cristian.auction_ms.exception.AuctionUnexpectedException;
import com.db.ar.cristian.auction_ms.exception.BidUnexpectedException;
import com.db.ar.cristian.auction_ms.exception.UnexpectedFeignException;
import com.db.ar.cristian.auction_ms.feign.FeignUserClient;
import com.db.ar.cristian.auction_ms.mapper.BidMapper;
import com.db.ar.cristian.auction_ms.messaging.producer.AuctionProducer;
import com.db.ar.cristian.auction_ms.model.Auction;
import com.db.ar.cristian.auction_ms.model.AuctionStatus;
import com.db.ar.cristian.auction_ms.model.Bid;
import com.db.ar.cristian.auction_ms.repository.AuctionRepository;
import com.db.ar.cristian.auction_ms.repository.BidRepository;
import com.db.ar.cristian.auction_ms.service.validation.AuctionBidValidation;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final BidMapper bidMapper;
    private final FeignUserClient feignUserClient;
    private final AuctionProducer auctionProducer;
    private final ApplicationEventPublisher eventPublisher;
    private final List<AuctionBidValidation> validations;

    @Transactional
    public BidResponse placeBid(Long auctionId, CreateBidRequest request) {
        Auction auction = auctionRepository.findByIdForUpdate(auctionId).orElseThrow(
                () -> new AuctionUnexpectedException("Leilão não encontrado com id: " + auctionId));
        Instant now = Instant.now();
        Utils.refreshStatus(auction, now);

        validations.forEach(val -> val.validateAuctionBid(auction, request));

        try {
            var userRep = feignUserClient.getUser(request.bidderId()).orElseThrow(() ->
                                 new UnexpectedFeignException("Usuário não encontrado, lance não registrado!"));

            if (request.amount().compareTo(userRep.accountLimit()) > 0){
                throw new BidUnexpectedException("Usuário não possui limite para o lance");
            }

            Bid bid = bidMapper.toEntity(request, auction, now);
            auction.setActualBid(bid.getAmount());
            auction.addBid(bid);
            bid = bidRepository.save(bid);
            BidResponse response = bidMapper.toResponse(bid);

            //envia evento de novo bid aos consumidores
            auctionProducer.newAuctionBid(response);

            //envia evento de dominio/aplicação... ainda não esta chamando o websocket
            eventPublisher.publishEvent(new BidUpdateDto(auction.getId(), auction.getActualBid()));

            return response;

        }catch (FeignException.NotFound e){
            throw new UnexpectedFeignException("User not found ");
        }
    }

    @Transactional
    public List<BidResponse> findBids(Long auctionId) {
        auctionRepository.findById(auctionId);
        return bidRepository.findAllByAuctionIdOrderByCreatedAtDesc(auctionId).stream()
                            .map(bidMapper::toResponse)
                            .toList();
    }



}
