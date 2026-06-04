package com.devgui.urlshortener.domain.service;

import com.devgui.urlshortener.domain.model.ShortUrl;
import com.devgui.urlshortener.domain.model.UrlAccessAnalytics;
import com.devgui.urlshortener.infrastructure.repository.UrlAccessAnalyticsRepository;
import org.springframework.stereotype.Service;

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
}
