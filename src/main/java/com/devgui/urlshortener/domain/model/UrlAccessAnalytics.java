package com.devgui.urlshortener.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "url_access_analytics")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UrlAccessAnalytics implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @Column(name = "referrer", length = 512)
    private String referrer;

    @ManyToOne
    @JoinColumn(name = "short_url_id", nullable = false)
    private ShortUrl shortUrl;

    @CreatedDate
    @Column(name = "accessed_at", nullable = false, updatable = false)
    private Instant accessedAt;

    public UrlAccessAnalytics(String userAgent, String referrer, ShortUrl shortUrl){
        this.userAgent = userAgent;
        this.referrer = referrer;
        this.shortUrl = shortUrl;
    }
}
