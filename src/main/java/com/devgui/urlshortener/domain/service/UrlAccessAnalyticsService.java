package com.devgui.urlshortener.domain.service;

import com.devgui.urlshortener.domain.model.ShortUrl;
import com.devgui.urlshortener.domain.model.UrlAccessAnalytics;
import com.devgui.urlshortener.infrastructure.repository.UrlAccessAnalyticsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UrlAccessAnalyticsService {

    private final UrlAccessAnalyticsRepository urlAccessAnalyticsRepository;

    public UrlAccessAnalyticsService(UrlAccessAnalyticsRepository urlAccessAnalyticsRepository) {
        this.urlAccessAnalyticsRepository = urlAccessAnalyticsRepository;
    }

    public void create(String userAgent, String referrer, ShortUrl shortUrl){
        UrlAccessAnalytics urlAccessAnalytics = new UrlAccessAnalytics(userAgent, referrer, shortUrl);
        urlAccessAnalyticsRepository.save(urlAccessAnalytics);
    }

    public List<UrlAccessAnalytics> getAnalytics(UUID shortUrlId, Integer size){
        Pageable pageable = PageRequest.of(0, size);
        return urlAccessAnalyticsRepository.findRecentAccessesByShortUrlId(shortUrlId, pageable);
    }
}
