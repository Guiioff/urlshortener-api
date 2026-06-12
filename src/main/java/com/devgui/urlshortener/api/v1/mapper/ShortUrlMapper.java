package com.devgui.urlshortener.api.v1.mapper;

import com.devgui.urlshortener.api.v1.dto.request.ShortUrlRequest;
import com.devgui.urlshortener.api.v1.dto.response.PageResponse;
import com.devgui.urlshortener.api.v1.dto.response.ShortUrlDetailsResponse;
import com.devgui.urlshortener.api.v1.dto.response.ShortUrlResponse;
import com.devgui.urlshortener.domain.model.ShortUrl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class ShortUrlMapper {

    public ShortUrl toEntity(ShortUrlRequest dto){
        if (dto == null) return null;

        String originalUrl = dto.originalUrl();
        Instant expiresAt = dto.expiresAt();

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

    public ShortUrlDetailsResponse toDetailsResponse(ShortUrl entity, String baseUrl){
        UUID id = entity.getId();
        String originalUrl = entity.getOriginalUrl();
        String shortUrl = baseUrl + "/" + entity.getShortKey();
        Integer clickCount = entity.getClickCount();
        boolean active = entity.isActive();
        Instant expiresAt = entity.getExpiresAt();
        Instant createdAt = entity.getCreatedAt();
        Instant updatedAt = entity.getUpdatedAt();

        return new ShortUrlDetailsResponse(
                id, originalUrl, shortUrl, clickCount, active, expiresAt, createdAt, updatedAt);
    }

    public PageResponse<ShortUrlResponse> toPageResponse(Page<ShortUrl> shortUrlPage, String baseUrl){
        List<ShortUrlResponse> shortUrlResponseList = shortUrlPage.getContent()
                .stream()
                .map(su -> this.toResponse(su, baseUrl))
                .toList();
        Integer page = shortUrlPage.getNumber();
        Integer size = shortUrlPage.getSize();
        Integer totalPages = shortUrlPage.getTotalPages();
        Long totalElements = shortUrlPage.getTotalElements();

        return new PageResponse<ShortUrlResponse>(shortUrlResponseList, page, size, totalPages, totalElements);
    }

}
