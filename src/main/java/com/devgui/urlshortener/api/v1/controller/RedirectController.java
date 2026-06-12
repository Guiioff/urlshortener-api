package com.devgui.urlshortener.api.v1.controller;

import com.devgui.urlshortener.domain.model.ShortUrl;
import com.devgui.urlshortener.domain.service.ShortUrlService;
import com.devgui.urlshortener.domain.service.UrlAccessAnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping
public class RedirectController {

    private final ShortUrlService shortUrlService;
    private final UrlAccessAnalyticsService urlAccessAnalyticsService;

    public RedirectController(ShortUrlService shortUrlService, UrlAccessAnalyticsService urlAccessAnalyticsService) {
        this.shortUrlService = shortUrlService;
        this.urlAccessAnalyticsService = urlAccessAnalyticsService;
    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirect(@PathVariable String shortKey, HttpServletRequest request){
        ShortUrl shortUrl = shortUrlService.incrementClicks(shortKey);

        String userAgent = request.getHeader("User-Agent");
        String referrer = request.getHeader("Referer");
        urlAccessAnalyticsService.create(userAgent, referrer, shortUrl);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(shortUrl.getOriginalUrl()))
                .build();
    }
}
