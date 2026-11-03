package com.db.ar.cristian.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 11)
    private String doc;

    @Column(precision = 19, scale = 2)
    private BigDecimal accountLimit;

    private UserRating userRating;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<BidReference> bids = new ArrayList<>();

    public void addBid(BidReference bidReference){
        bids.add(bidReference);
        bidReference.setUser(this);
    }
}
