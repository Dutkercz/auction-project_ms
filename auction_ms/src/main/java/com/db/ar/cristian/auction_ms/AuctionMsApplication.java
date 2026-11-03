package com.db.ar.cristian.auction_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuctionMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionMsApplication.class, args);
    }

}
