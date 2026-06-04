package com.devgui.urlshortener.infrastructure.repository;

import com.devgui.urlshortener.domain.model.UrlAccessAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UrlAccessAnalyticsRepository extends JpaRepository<UrlAccessAnalytics, UUID> {
}
