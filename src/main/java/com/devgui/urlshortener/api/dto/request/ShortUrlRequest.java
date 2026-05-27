package com.devgui.urlshortener.api.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ShortUrlRequest(
        @NotBlank(message = "A URL original é obrigatória")
        String originalUrl,

        @Future(message = "A Data de Expiração deve ser no futuro")
        LocalDateTime expiresAt
) {
}
