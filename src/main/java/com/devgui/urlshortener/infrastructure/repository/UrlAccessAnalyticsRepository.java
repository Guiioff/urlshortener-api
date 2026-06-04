package com.devgui.urlshortener.infrastructure.repository;

import com.devgui.urlshortener.domain.model.UrlAccessAnalytics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UrlAccessAnalyticsRepository extends JpaRepository<UrlAccessAnalytics, UUID> {
    @Query("""
    SELECT uaa FROM UrlAccessAnalytics uaa
    WHERE uaa.shortUrl.id = :id
    ORDER BY uaa.accessedAt DESC
    """)
    List<UrlAccessAnalytics> findRecentAccessesByShortUrlId(@Param("id") UUID id, Pageable pageable);
}
