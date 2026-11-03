package com.db.ar.cristian.user.dto;

import com.db.ar.cristian.user.model.UserRating;

import java.math.BigDecimal;

public record UserResponse(
        Long id,
        BigDecimal accountLimit,
        UserRating userRating
) {
}
