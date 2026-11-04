package com.db.ar.cristian.auction_ms.messaging.event;

import com.db.ar.cristian.auction_ms.dto.BidUpdateDto;
import com.db.ar.cristian.auction_ms.service.AuctionNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AuctionEventListener {

    private final AuctionNotificationService notificationService;

    ///BiDService executa a operação de validação, bloqueio e commit no banco de dados.
    /// O evento é publicado a nivel de aplicação/dominio com a injeção do ApplicationEventPublisher no BidService
    /// como estarmos usando o AFTER_COMMIT esse Listener recebe o evento após o commit
    /// por fim chamamos o WebSocket que esta injetado AuctionNotificationService que distribuir a atualização pro front
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBidUpdateEvent(BidUpdateDto updateDto){
        notificationService.notifyBidUpdate(updateDto.auctionId(), updateDto);
    }

    ///Existem ténicas mais confiaveis para sistema distribuidos como TransactionalOutbox que oferecem mais segurança
    /// em caso de falha no serviço
}
