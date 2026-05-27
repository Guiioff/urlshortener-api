package com.devgui.urlshortener.api.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ShortUrlResponse(
        UUID id,
        String originalUrl,
        String shortUrl,
        Instant createdAt,
        Instant expiresAt
) {
}
