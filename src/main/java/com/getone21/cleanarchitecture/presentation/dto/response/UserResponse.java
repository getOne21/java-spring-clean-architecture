package com.getone21.cleanarchitecture.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
