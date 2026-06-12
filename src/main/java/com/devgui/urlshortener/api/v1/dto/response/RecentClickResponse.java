package com.devgui.urlshortener.api.v1.dto.response;

import java.time.Instant;

public record RecentClickResponse(
        String userAgent,
        String referrer,
        Instant accessedAt
) {
}
