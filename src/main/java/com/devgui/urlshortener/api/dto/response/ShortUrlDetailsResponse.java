package com.devgui.urlshortener.api.dto.response;

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
