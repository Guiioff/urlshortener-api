package com.devgui.urlshortener.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "short_urls")
@EntityListeners(AuditingEntityListener.class)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "original_url", length = 2048, nullable = false)
    private String originalUrl;

    @Column(name = "short_key", length = 20, nullable = false, unique = true)
    private String shortKey;

    @Column(name = "click_count", nullable = false)
    private Integer clickCount;

    @Column(name = "active", nullable = false)
    private boolean active;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    public ShortUrl(String originalUrl, String shortKey, Instant expiresAt, Integer clickCount, boolean active) {
        this.originalUrl = originalUrl;
        this.shortKey = shortKey;
        this.expiresAt = expiresAt;
        this.clickCount = clickCount;
        this.active = active;
    }

    public ShortUrl(String originalUrl, Instant expiresAt) {
        this.originalUrl = originalUrl;
        this.expiresAt = expiresAt;
        this.clickCount = 0;
        this.active = true;
    }

    public ShortUrl withShortKey(String shortKey) {
        return new ShortUrl(this.originalUrl, shortKey, this.expiresAt, this.clickCount, this.active);
    }

    public ShortUrl incrementClickCount() {
        return new ShortUrl(
                this.id,
                this.originalUrl,
                this.shortKey,
                this.clickCount + 1,
                this.active,
                this.createdAt,
                this.updatedAt,
                this.expiresAt
        );
    }
}
