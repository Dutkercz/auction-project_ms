package com.db.ar.cristian.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BidReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bidId;
    private Long auctionId;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
}
