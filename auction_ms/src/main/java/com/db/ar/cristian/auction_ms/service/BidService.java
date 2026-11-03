package com.db.ar.cristian.auction_ms.service;

import com.db.ar.cristian.auction_ms.dto.BidResponse;
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
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public BidResponse placeBid(Long auctionId, CreateBidRequest request) {
        Auction auction = auctionRepository.findByIdForUpdate(auctionId).orElseThrow(
                () -> new AuctionUnexpectedException("Leilão não encontrado com id: " + auctionId));
        Instant now = Instant.now();
        Utils.refreshStatus(auction, now);
        if (auction.getStatus() != AuctionStatus.OPEN) {
            throw new AuctionConflictException("O leilão não está aberto para lances.");
        }
        if (auction.getSellerId().equals(request.bidderId())) {
            throw new BidUnexpectedException("O vendedor não pode dar lances no próprio leilão.");
        }
        if (request.amount().compareTo(auction.getMinimumBid()) < 0 ||
           (auction.getActualBid() != null && request.amount().compareTo(auction.getActualBid()) <= 0)) {
            throw new BidUnexpectedException("O lance deve ser maior que o atual."
            );
        }
        if (!auction.getBids().isEmpty() &&
            Objects.equals(request.bidderId(), auction.getBids().getLast().getBidderId())){
            throw new BidUnexpectedException("O lance atual já pertence a este usuário");
        }

        //todo: verificar via feign cadastro de usuario e se ele possui "limite" para o lance
        try {
            var userRep = feignUserClient.getUser(request.bidderId());

            if (userRep == null){
                throw new UnexpectedFeignException("Usuário não encontrado, lance não registrado!");
            }
            if (request.amount().compareTo(userRep.accountLimit()) > 0){
                throw new BidUnexpectedException("Usuário não possui limite para o lance");
            }

            Bid bid = bidMapper.toEntity(request, auction, now);
            auction.setActualBid(request.amount());
            auction.addBid(bid);
            bid = bidRepository.save(bid);
            BidResponse response = bidMapper.toResponse(bid);

            //envia evento de novo bid aos consumidores
            auctionProducer.newAuctionBid(response);
            return response;

        }catch (FeignException.NotFound e){
            throw new UnexpectedFeignException(e.getLocalizedMessage());
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
