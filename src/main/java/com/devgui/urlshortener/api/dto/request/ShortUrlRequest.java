package com.devgui.urlshortener.api.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

public record ShortUrlRequest(
        @NotBlank(message = "Original URL is required.")
        @URL(message = "Invalid URL format.")
        String originalUrl,

        @Future(message = "Expiration date must be in the future.")
        LocalDateTime expiresAt
) {
}
