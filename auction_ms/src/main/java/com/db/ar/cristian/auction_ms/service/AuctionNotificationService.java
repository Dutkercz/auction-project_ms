package com.db.ar.cristian.auction_ms.service;

import com.db.ar.cristian.auction_ms.dto.BidUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

//    Não é preciso criar um Controller para o websocket,
//    o spring disponibiliza esse componente para enviar as mensagens
    public void notifyBidUpdate(Long auctionId, BidUpdateDto bidUpdateDto){
        messagingTemplate.convertAndSend("/topic/auctions/"+auctionId, bidUpdateDto);
    }
}
