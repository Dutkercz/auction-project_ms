package com.db.ar.cristian.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank
        String doc
) {
}
