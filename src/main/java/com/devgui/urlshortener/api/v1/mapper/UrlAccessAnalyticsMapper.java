package com.devgui.urlshortener.api.v1.mapper;

import com.devgui.urlshortener.api.v1.dto.response.RecentClickResponse;
import com.devgui.urlshortener.api.v1.dto.response.UrlAnalyticsResponse;
import com.devgui.urlshortener.domain.model.ShortUrl;
import com.devgui.urlshortener.domain.model.UrlAccessAnalytics;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UrlAccessAnalyticsMapper {

    public UrlAnalyticsResponse toResponse(
            ShortUrl shortUrl, List<UrlAccessAnalytics> urlAccessAnalyticsList){
        String shortKey = shortUrl.getShortKey();
        String originalUrl = shortUrl.getOriginalUrl();
        Integer clickCount = shortUrl.getClickCount();
        List<RecentClickResponse> recentClicks = urlAccessAnalyticsList
                .stream()
                .map(uaa -> new RecentClickResponse(
                        uaa.getUserAgent(),
                        uaa.getReferrer(),
                        uaa.getAccessedAt()))
                .toList();

        return new UrlAnalyticsResponse(shortKey, originalUrl, clickCount, recentClicks);
    }
}
