package com.devgui.urlshortener.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "short_urls")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "original_url", length = 2048, nullable = false)
    private String originalUrl;

    @Column(name = "short_key", length = 20, nullable = false, unique = true)
    private String shortKey;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    public ShortUrl(String originalUrl, String shortKey, Instant expiresAt) {
        this.originalUrl = originalUrl;
        this.shortKey = shortKey;
        this.expiresAt = expiresAt;
    }

    public ShortUrl withShortKey(String shortKey) {
        return new ShortUrl(this.originalUrl, shortKey, this.expiresAt);
    }
}
