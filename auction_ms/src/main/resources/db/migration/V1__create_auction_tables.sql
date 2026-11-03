CREATE TABLE auctions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(120) NOT NULL,
    description VARCHAR(2000),
    minimum_bid DECIMAL(19, 2) NOT NULL,
    actual_bid DECIMAL(19, 2),
    winner_bid DECIMAL(19, 2),
    seller_id BIGINT NOT NULL,
    winner_id BIGINT,
    starts_at TIMESTAMP(6) NOT NULL,
    ends_at TIMESTAMP(6) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    status VARCHAR(16) NOT NULL
);

CREATE TABLE bids (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    amount DECIMAL(19, 2) NOT NULL,
    bidder_id BIGINT NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    auction_id BIGINT NOT NULL,

    INDEX idx_bids_auction_created (auction_id, created_at),
    CONSTRAINT fk_bids_auction FOREIGN KEY (auction_id) REFERENCES auctions (id) ON DELETE CASCADE
);
