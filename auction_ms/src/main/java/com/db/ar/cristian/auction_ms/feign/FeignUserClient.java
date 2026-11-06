package com.db.ar.cristian.auction_ms.feign;

import lombok.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Optional;

@FeignClient(url = "http://localhost:8081/api/v1/users", name = "auction.users")
public interface FeignUserClient {

    @GetMapping("/{id}")
    Optional<UserRepresentation> getUser(@PathVariable Long id);


    record UserRepresentation (
            Long id,
            UserRating userRating,
            BigDecimal accountLimit){
    }

    enum UserRating {
        EXCELLENT,
        GOOD,
        REGULAR,
        ALERT,
        SUSPENDED;
    }
}
