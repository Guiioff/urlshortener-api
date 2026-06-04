package com.devgui.urlshortener.api.controller;

import com.devgui.urlshortener.api.dto.request.ShortUrlRequest;
import com.devgui.urlshortener.api.dto.response.PageResponse;
import com.devgui.urlshortener.api.dto.response.ShortUrlDetailsResponse;
import com.devgui.urlshortener.api.dto.response.ShortUrlResponse;
import com.devgui.urlshortener.api.dto.response.UrlAnalyticsResponse;
import com.devgui.urlshortener.api.mapper.ShortUrlMapper;
import com.devgui.urlshortener.api.mapper.UrlAccessAnalyticsMapper;
import com.devgui.urlshortener.domain.model.ShortUrl;
import com.devgui.urlshortener.domain.model.UrlAccessAnalytics;
import com.devgui.urlshortener.domain.service.ShortUrlService;
import com.devgui.urlshortener.domain.service.UrlAccessAnalyticsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/urls")
public class ShortUrlController {

    private final ShortUrlService shortUrlService;
    private final UrlAccessAnalyticsService urlAccessAnalyticsService;
    private final ShortUrlMapper shortUrlMapper;
    private final UrlAccessAnalyticsMapper urlAccessAnalyticsMapper;

    public ShortUrlController(ShortUrlService shortUrlService, UrlAccessAnalyticsService urlAccessAnalyticsService, ShortUrlMapper shortUrlMapper, UrlAccessAnalyticsMapper urlAccessAnalyticsMapper) {
        this.shortUrlService = shortUrlService;
        this.urlAccessAnalyticsService = urlAccessAnalyticsService;
        this.shortUrlMapper = shortUrlMapper;
        this.urlAccessAnalyticsMapper = urlAccessAnalyticsMapper;
    }

    @PostMapping
    public ResponseEntity<ShortUrlResponse> shorten(@RequestBody @Valid ShortUrlRequest dto){
        ShortUrl shortUrl = shortUrlMapper.toEntity(dto);
        ShortUrl shortUrlSaved = shortUrlService.shorten(shortUrl);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

        ShortUrlResponse shortUrlResponse = shortUrlMapper.toResponse(shortUrlSaved, baseUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrlResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShortUrlDetailsResponse> getById(@PathVariable UUID id){
        ShortUrl shortUrl = shortUrlService.getById(id);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

        ShortUrlDetailsResponse shortUrlResponse = shortUrlMapper.toDetailsResponse(shortUrl, baseUrl);
        return ResponseEntity.ok(shortUrlResponse);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ShortUrlResponse>> getAll(
            @RequestParam(value = "page", defaultValue = "0", required = false)
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false)
            @Max(value = 20, message = "Size must not exceed 20")
            Integer size
    ){
        Page<ShortUrl> shortUrlPage = shortUrlService.getAll(page, size);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

        PageResponse<ShortUrlResponse> response = shortUrlMapper.toPageResponse(shortUrlPage, baseUrl);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disable(@PathVariable UUID id){
        shortUrlService.disable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<UrlAnalyticsResponse> analytics(
            @PathVariable
            UUID id,
            @RequestParam(value = "size", defaultValue = "5", required = false)
            @Max(value = 10, message = "Size must not exceed 10")
            Integer size){
        ShortUrl shortUrl = shortUrlService.getById(id);
        List<UrlAccessAnalytics> urlAccessAnalyticsList = urlAccessAnalyticsService.getAnalytics(shortUrl.getId(), size);
        UrlAnalyticsResponse response = urlAccessAnalyticsMapper.toResponse(shortUrl, urlAccessAnalyticsList);
        return ResponseEntity.ok(response);
    }
}
