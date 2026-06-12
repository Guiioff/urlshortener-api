package com.devgui.urlshortener.api.v1.dto.response;

import java.util.List;

public record UrlAnalyticsResponse(
        String shortKey,
        String originalUrl,
        Integer clickCount,
        List<RecentClickResponse> recentClicks
) {
}
