package com.devgui.urlshortener.factory;

import com.devgui.urlshortener.domain.model.ShortUrl;

import java.time.Instant;

public final class ShortUrlFactory {

    private static final String DEFAULT_URL = "google.com";
    private static final String DEFAULT_SHORT_KEY = "abc123";
    private static final Instant DEFAULT_EXPIRATION =
            Instant.now().plusSeconds(86400);

    private ShortUrlFactory() {
    }

    public static ShortUrl create() {
        return new ShortUrl(
                DEFAULT_URL,
                DEFAULT_EXPIRATION
        );
    }

    public static ShortUrl create(String originalUrl) {
        return new ShortUrl(
                originalUrl,
                DEFAULT_EXPIRATION
        );
    }

    public static ShortUrl create(Instant expiresAt) {
        return new ShortUrl(
                DEFAULT_URL,
                expiresAt
        );
    }

    public static ShortUrl create(
            String originalUrl,
            Instant expiresAt
    ) {
        return new ShortUrl(
                originalUrl,
                expiresAt
        );
    }

    public static ShortUrl withoutExpiration() {
        return new ShortUrl(
                DEFAULT_URL,
                null
        );
    }

    public static ShortUrl expired() {
        return new ShortUrl(
                DEFAULT_URL,
                Instant.now().minusSeconds(86400)
        );
    }

    public static ShortUrl createComplete() {
        return new ShortUrl(
                DEFAULT_URL,
                DEFAULT_SHORT_KEY,
                DEFAULT_EXPIRATION,
                0,
                true
        );
    }

    public static ShortUrl createComplete(String originalUrl) {
        return new ShortUrl(
                originalUrl,
                DEFAULT_SHORT_KEY,
                DEFAULT_EXPIRATION,
                0,
                true
        );
    }

    public static ShortUrl createComplete(
            String originalUrl,
            String shortKey
    ) {
        return new ShortUrl(
                originalUrl,
                shortKey,
                DEFAULT_EXPIRATION,
                0,
                true
        );
    }

    public static ShortUrl createComplete(
            String originalUrl,
            String shortKey,
            Instant expiresAt
    ) {
        return new ShortUrl(
                originalUrl,
                shortKey,
                expiresAt,
                0,
                true
        );
    }

    public static ShortUrl createComplete(
            String originalUrl,
            String shortKey,
            Instant expiresAt,
            Integer clickCount,
            boolean active
    ) {
        return new ShortUrl(
                originalUrl,
                shortKey,
                expiresAt,
                clickCount,
                active
        );
    }

    public static ShortUrl inactive() {
        return new ShortUrl(
                DEFAULT_URL,
                DEFAULT_SHORT_KEY,
                DEFAULT_EXPIRATION,
                0,
                false
        );
    }

    public static ShortUrl withClicks(Integer clickCount) {
        return new ShortUrl(
                DEFAULT_URL,
                DEFAULT_SHORT_KEY,
                DEFAULT_EXPIRATION,
                clickCount,
                true
        );
    }

    public static ShortUrl completeExpired() {
        return new ShortUrl(
                DEFAULT_URL,
                DEFAULT_SHORT_KEY,
                Instant.now().minusSeconds(86400),
                0,
                true
        );
    }
}