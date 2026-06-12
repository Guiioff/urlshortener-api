package com.devgui.urlshortener.api.v1.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ShortUrlDetailsResponse(
        UUID id,
        String originalUrl,
        String shortUrl,
        Integer clickCount,
        boolean active,
        Instant expiresAt,
        Instant createdAt,
        Instant updatedAt
) {
}
