package com.devgui.urlshortener.api.dto.response;

import java.time.Instant;

public record RecentClickResponse(
        String userAgent,
        String referrer,
        Instant accessedAt
) {
}
