package com.devgui.urlshortener.api.mapper;

import com.devgui.urlshortener.api.dto.request.ShortUrlRequest;
import com.devgui.urlshortener.api.dto.response.ShortUrlResponse;
import com.devgui.urlshortener.domain.model.ShortUrl;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class ShortUrlMapper {

    public ShortUrl toEntity(ShortUrlRequest dto){
        if (dto == null) return null;

        String originalUrl = dto.originalUrl();
        Instant expiresAt = dto.expiresAt() != null
                ? dto.expiresAt().toInstant(ZoneOffset.UTC)
                : null;

        return new ShortUrl(originalUrl, expiresAt);
    }

    public ShortUrlResponse toResponse(ShortUrl entity, String baseUrl){
        UUID id = entity.getId();
        String originalUrl = entity.getOriginalUrl();
        String shortUrl = baseUrl + "/" + entity.getShortKey();
        Instant createdAt = entity.getCreatedAt();
        Instant expiresAt = entity.getExpiresAt();

        return new ShortUrlResponse(id, originalUrl, shortUrl, createdAt, expiresAt);
    }
}
