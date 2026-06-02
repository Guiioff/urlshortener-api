package com.devgui.urlshortener.api.controller;

import com.devgui.urlshortener.api.dto.request.ShortUrlRequest;
import com.devgui.urlshortener.api.dto.response.ShortUrlDetailsResponse;
import com.devgui.urlshortener.api.dto.response.ShortUrlResponse;
import com.devgui.urlshortener.api.mapper.ShortUrlMapper;
import com.devgui.urlshortener.domain.model.ShortUrl;
import com.devgui.urlshortener.domain.service.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/urls")
public class ShortUrlController {

    private final ShortUrlService shortUrlService;
    private final ShortUrlMapper shortUrlMapper;

    public ShortUrlController(ShortUrlService shortUrlService, ShortUrlMapper shortUrlMapper) {
        this.shortUrlService = shortUrlService;
        this.shortUrlMapper = shortUrlMapper;
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

}
